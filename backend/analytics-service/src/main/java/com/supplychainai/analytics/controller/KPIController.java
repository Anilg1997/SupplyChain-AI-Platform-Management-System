package com.supplychainai.analytics.controller;

import com.supplychainai.analytics.model.KPI;
import com.supplychainai.analytics.repository.KPIRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kpis")
public class KPIController {

    private final KPIRepository repository;

    public KPIController(KPIRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<KPI>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KPI> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("KPI not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<KPI> create(@RequestBody KPI entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KPI> update(@PathVariable UUID id, @RequestBody KPI entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
