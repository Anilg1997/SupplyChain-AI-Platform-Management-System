package com.supplychainai.planning.controller;

import com.supplychainai.planning.model.SupplyPlan;
import com.supplychainai.planning.repository.SupplyPlanRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/supply-plans")
public class SupplyPlanController {

    private final SupplyPlanRepository repository;

    public SupplyPlanController(SupplyPlanRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<SupplyPlan>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyPlan> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplyPlan not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<SupplyPlan> create(@RequestBody SupplyPlan entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyPlan> update(@PathVariable UUID id, @RequestBody SupplyPlan entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
