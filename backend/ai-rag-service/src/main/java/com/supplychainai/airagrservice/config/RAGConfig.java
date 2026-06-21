package com.supplychainai.airagrservice.config;

import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RAGConfig {

    @Bean
    public DocumentParser documentParser() {
        return new TextDocumentParser();
    }

    @Bean
    public dev.langchain4j.data.document.splitter.DocumentSplitter documentSplitter() {
        return DocumentSplitters.recursive(500, 100);
    }

    @Bean
    public OllamaEmbeddingModel embeddingModel(@Value("${OLLAMA_BASE_URL:http://ollama:11434}") String baseUrl,
                                                @Value("${OLLAMA_EMBEDDING_MODEL:nomic-embed-text}") String modelName) {
        return OllamaEmbeddingModel.builder()
            .baseUrl(baseUrl)
            .modelName(modelName)
            .timeout(Duration.ofMinutes(2))
            .build();
    }

    @Bean
    public OllamaChatModel chatModel(@Value("${OLLAMA_BASE_URL:http://ollama:11434}") String baseUrl,
                                      @Value("${OLLAMA_CHAT_MODEL:llama3.1}") String modelName) {
        return OllamaChatModel.builder()
            .baseUrl(baseUrl)
            .modelName(modelName)
            .temperature(0.7)
            .topP(0.9)
            .timeout(Duration.ofMinutes(2))
            .build();
    }

    @Bean
    public PgVectorEmbeddingStore embeddingStore(@Value("${spring.ai.vectorstore.pgvector.dimensions:768}") int dimensions,
                                                  @Value("${spring.ai.vectorstore.pgvector.index-type:HNSW}") String indexType,
                                                  @Value("${spring.ai.vectorstore.pgvector.distance-type:COSINE_DISTANCE}") String distanceType) {
        return PgVectorEmbeddingStore.builder()
            .dimensions(dimensions)
            .indexType(indexType)
            .distanceType(distanceType)
            .build();
    }
}