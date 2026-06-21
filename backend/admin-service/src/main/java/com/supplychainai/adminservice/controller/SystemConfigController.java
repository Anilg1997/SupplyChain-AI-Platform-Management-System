package com.supplychainai.adminservice.controller;

import com.supplychainai.adminservice.dto.ConfigRequest;
import com.supplychainai.adminservice.model.SystemConfig;
import com.supplychainai.adminservice.service.SystemConfigService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/config")
public class SystemConfigController {

    private final SystemConfigService service;

    public SystemConfigController(SystemConfigService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SystemConfig>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/map")
    public ResponseEntity<Map<String, String>> getAllAsMap() {
        return ResponseEntity.ok(service.getAllAsMap());
    }

    @GetMapping("/{configKey}")
    public ResponseEntity<SystemConfig> findByKey(@PathVariable String configKey) {
        return ResponseEntity.ok(service.findByKey(configKey));
    }

    @PutMapping("/{configKey}")
    public ResponseEntity<SystemConfig> update(@PathVariable String configKey, @Valid @RequestBody ConfigRequest request) {
        return ResponseEntity.ok(service.update(configKey, request));
    }

    @PostMapping("/{configKey}")
    public ResponseEntity<SystemConfig> create(@PathVariable String configKey, @Valid @RequestBody ConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(configKey, request));
    }

    @DeleteMapping("/{configKey}")
    public ResponseEntity<Void> delete(@PathVariable String configKey) {
        service.delete(configKey);
        return ResponseEntity.noContent().build();
    }
}
