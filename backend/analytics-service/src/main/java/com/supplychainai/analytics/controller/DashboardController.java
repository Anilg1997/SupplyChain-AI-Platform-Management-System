package com.supplychainai.analytics.controller;

import com.supplychainai.analytics.model.Dashboard;
import com.supplychainai.analytics.repository.DashboardRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
public class DashboardController {

    private final DashboardRepository repository;

    public DashboardController(DashboardRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Dashboard>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dashboard> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dashboard not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<Dashboard> create(@RequestBody Dashboard entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dashboard> update(@PathVariable UUID id, @RequestBody Dashboard entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
