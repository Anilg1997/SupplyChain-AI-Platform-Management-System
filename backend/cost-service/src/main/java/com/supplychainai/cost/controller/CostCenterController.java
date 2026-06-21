package com.supplychainai.cost.controller;

import com.supplychainai.cost.model.CostCenter;
import com.supplychainai.cost.repository.CostCenterRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cost-centers")
public class CostCenterController {

    private final CostCenterRepository repository;

    public CostCenterController(CostCenterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<CostCenter>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostCenter> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CostCenter not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<CostCenter> create(@RequestBody CostCenter entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostCenter> update(@PathVariable UUID id, @RequestBody CostCenter entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
