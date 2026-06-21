package com.supplychainai.adminservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIModelService {

    private static final Logger log = LoggerFactory.getLogger(AIModelService.class);

    private final RestTemplate restTemplate;

    @Value("${ai.ollama.base-url:http://ollama:11434}")
    private String ollamaBaseUrl;

    public AIModelService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listModels() {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                ollamaBaseUrl + "/api/tags", Map.class);
            if (response != null && response.containsKey("models")) {
                return (List<Map<String, Object>>) response.get("models");
            }
        } catch (Exception e) {
            log.warn("Failed to fetch Ollama models: {}", e.getMessage());
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> pullModel(String modelName) {
        try {
            Map<String, Object> request = Map.of("name", modelName, "stream", false);
            Map<String, Object> response = restTemplate.postForObject(
                ollamaBaseUrl + "/api/pull", request, Map.class);
            return response != null ? response : Map.of("status", "error", "message", "No response");
        } catch (Exception e) {
            log.error("Failed to pull model {}: {}", modelName, e.getMessage());
            throw new RuntimeException("Failed to pull model: " + modelName, e);
        }
    }

    public Map<String, Object> deleteModel(String modelName) {
        try {
            restTemplate.delete(ollamaBaseUrl + "/api/delete?name=" + modelName);
            return Map.of("status", "deleted", "model", modelName);
        } catch (Exception e) {
            log.error("Failed to delete model {}: {}", modelName, e.getMessage());
            throw new RuntimeException("Failed to delete model: " + modelName, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> generateEmbedding(String model, String input) {
        try {
            Map<String, Object> request = Map.of("model", model, "prompt", input);
            Map<String, Object> response = restTemplate.postForObject(
                ollamaBaseUrl + "/api/embeddings", request, Map.class);
            return response != null ? response : Map.of("error", "No response");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> generateCompletion(String model, String prompt) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("prompt", prompt);
            request.put("stream", false);
            request.put("options", Map.of("temperature", 0.7, "top_p", 0.9));
            Map<String, Object> response = restTemplate.postForObject(
                ollamaBaseUrl + "/api/generate", request, Map.class);
            return response != null ? response : Map.of("error", "No response");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate completion", e);
        }
    }

    public Map<String, Object> getOllamaStatus() {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                ollamaBaseUrl, Map.class);
            return response != null ? response : Map.of("status", "unknown");
        } catch (Exception e) {
            return Map.of("status", "unreachable", "error", e.getMessage());
        }
    }
}
