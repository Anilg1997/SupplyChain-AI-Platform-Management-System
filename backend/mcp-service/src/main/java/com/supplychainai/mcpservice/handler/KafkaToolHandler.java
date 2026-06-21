package com.supplychainai.mcpservice.handler;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaToolHandler implements MCPToolHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaToolHandler.class);

    @Value("${spring.kafka.bootstrap-servers:kafka:29092}")
    private String bootstrapServers;

    @Override
    public String getServerName() { return "kafka"; }

    @Override
    public List<String> getAvailableTools() {
        return List.of("list_topics", "describe_topic", "produce_message", "consume_messages", "topic_info", "consumer_groups");
    }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Object executeTool(String tool, Map<String, Object> parameters) {
        return switch (tool) {
            case "list_topics" -> listTopics();
            case "describe_topic" -> describeTopic(parameters);
            case "produce_message" -> produceMessage(parameters);
            case "consume_messages" -> consumeMessages(parameters);
            case "topic_info" -> topicInfo(parameters);
            case "consumer_groups" -> consumerGroups();
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    private AdminClient adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return AdminClient.create(props);
    }

    private List<String> listTopics() {
        try (AdminClient admin = adminClient()) {
            return admin.listTopics().names().get().stream()
                .sorted()
                .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to list topics", e);
        }
    }

    private Map<String, Object> describeTopic(Map<String, Object> params) {
        String topic = (String) params.get("topic");
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Topic name is required");
        }
        try (AdminClient admin = adminClient()) {
            TopicDescription desc = admin.describeTopics(List.of(topic))
                .topicNameValues().get(topic).get();
            List<Map<String, Object>> partitions = desc.partitions().stream().map(p -> {
                Map<String, Object> part = new HashMap<>();
                part.put("partition", p.partition());
                part.put("leader", p.leader().id());
                part.put("replicas", p.replicas().stream().map(r -> r.id()).collect(Collectors.toList()));
                part.put("isr", p.isr().stream().map(r -> r.id()).collect(Collectors.toList()));
                return part;
            }).collect(Collectors.toList());
            return Map.of("topic", topic, "partitions", partitions.size(), "partitionDetails", partitions);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to describe topic", e);
        }
    }

    private Map<String, Object> produceMessage(Map<String, Object> params) {
        String topic = (String) params.get("topic");
        String key = (String) params.get("key");
        String value = (String) params.get("value");
        if (topic == null || value == null) {
            throw new IllegalArgumentException("Topic and value are required");
        }
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = key != null
                ? new ProducerRecord<>(topic, key, value)
                : new ProducerRecord<>(topic, value);
            RecordMetadata metadata = producer.send(record).get();
            return Map.of(
                "topic", metadata.topic(),
                "partition", metadata.partition(),
                "offset", metadata.offset(),
                "timestamp", metadata.timestamp()
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to produce message", e);
        }
    }

    private List<Map<String, Object>> consumeMessages(Map<String, Object> params) {
        String topic = (String) params.get("topic");
        int count = (int) params.getOrDefault("count", 10);
        String groupId = (String) params.getOrDefault("groupId", "mcp-consumer-" + UUID.randomUUID());
        String offsetReset = (String) params.getOrDefault("offsetReset", "earliest");
        if (topic == null) {
            throw new IllegalArgumentException("Topic is required");
        }
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        List<Map<String, Object>> messages = new ArrayList<>();
        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of(topic));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, String> record : records) {
                if (messages.size() >= count) break;
                messages.add(Map.of(
                    "key", record.key(),
                    "value", record.value(),
                    "partition", record.partition(),
                    "offset", record.offset(),
                    "timestamp", record.timestamp()
                ));
            }
            return messages;
        }
    }

    private Map<String, Object> topicInfo(Map<String, Object> params) {
        String topic = (String) params.get("topic");
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Topic name is required");
        }
        try (AdminClient admin = adminClient()) {
            Config config = admin.describeConfigs(Map.of(
                new ConfigResource(ConfigResource.Type.TOPIC, topic),
                new ConfigResource(ConfigResource.Type.TOPIC, topic)
            )).values().entrySet().stream()
                .filter(e -> e.getKey().name().equals(topic))
                .findFirst()
                .map(e -> { try { return e.getValue().get(); } catch (Exception ex) { return null; } })
                .orElse(null);
            Map<String, String> configMap = new HashMap<>();
            if (config != null) {
                config.entries().forEach(e -> configMap.put(e.name(), e.value()));
            }
            return Map.of("topic", topic, "config", configMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get topic info", e);
        }
    }

    private List<String> consumerGroups() {
        try (AdminClient admin = adminClient()) {
            return admin.listConsumerGroups().all().get().stream()
                .map(g -> g.groupId())
                .sorted()
                .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to list consumer groups", e);
        }
    }
}
