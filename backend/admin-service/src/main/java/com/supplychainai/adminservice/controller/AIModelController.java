package com.supplychainai.adminservice.controller;

import com.supplychainai.adminservice.service.AIModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/ai")
public class AIModelController {

    private final AIModelService service;

    public AIModelController(AIModelService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getOllamaStatus() {
        return ResponseEntity.ok(service.getOllamaStatus());
    }

    @GetMapping("/models")
    public ResponseEntity<List<Map<String, Object>>> listModels() {
        return ResponseEntity.ok(service.listModels());
    }

    @PostMapping("/models/pull")
    public ResponseEntity<Map<String, Object>> pullModel(@RequestBody Map<String, String> body) {
        String modelName = body.get("name");
        if (modelName == null || modelName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Model name is required"));
        }
        return ResponseEntity.ok(service.pullModel(modelName));
    }

    @DeleteMapping("/models/{modelName}")
    public ResponseEntity<Map<String, Object>> deleteModel(@PathVariable String modelName) {
        return ResponseEntity.ok(service.deleteModel(modelName));
    }

    @PostMapping("/embed")
    public ResponseEntity<Map<String, Object>> generateEmbedding(@RequestBody Map<String, String> body) {
        String model = body.getOrDefault("model", "nomic-embed-text");
        String input = body.get("input");
        if (input == null || input.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Input text is required"));
        }
        return ResponseEntity.ok(service.generateEmbedding(model, input));
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateCompletion(@RequestBody Map<String, String> body) {
        String model = body.getOrDefault("model", "llama3.1");
        String prompt = body.get("prompt");
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Prompt is required"));
        }
        return ResponseEntity.ok(service.generateCompletion(model, prompt));
    }
}
