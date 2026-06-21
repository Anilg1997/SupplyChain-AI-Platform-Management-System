package com.supplychainai.mcpservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MCPConfig {

    @Value("${mcp.servers.filesystem:true}")
    private boolean filesystemEnabled;

    @Value("${mcp.servers.git:true}")
    private boolean gitEnabled;

    @Value("${mcp.servers.postgres:true}")
    private boolean postgresEnabled;

    @Value("${mcp.servers.kafka:true}")
    private boolean kafkaEnabled;

    @Value("${spring.datasource.url:jdbc:postgresql://postgres:5432/supplychainai}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:postgres}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:SupplyChain@2024}")
    private String datasourcePassword;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Map<String, Boolean> mcpServerConfig() {
        Map<String, Boolean> config = new HashMap<>();
        config.put("filesystem", filesystemEnabled);
        config.put("git", gitEnabled);
        config.put("postgres", postgresEnabled);
        config.put("kafka", kafkaEnabled);
        return config;
    }

    public String getDatasourceUrl() { return datasourceUrl; }
    public String getDatasourceUsername() { return datasourceUsername; }
    public String getDatasourcePassword() { return datasourcePassword; }
}
