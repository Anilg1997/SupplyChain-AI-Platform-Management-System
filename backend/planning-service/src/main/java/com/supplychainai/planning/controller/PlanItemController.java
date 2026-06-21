package com.supplychainai.planning.controller;

import com.supplychainai.planning.model.PlanItem;
import com.supplychainai.planning.repository.PlanItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plan-items")
public class PlanItemController {

    private final PlanItemRepository repository;

    public PlanItemController(PlanItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PlanItem>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanItem> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlanItem not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<PlanItem> create(@RequestBody PlanItem entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanItem> update(@PathVariable UUID id, @RequestBody PlanItem entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
