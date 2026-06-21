package com.supplychainai.pr.controller;

import com.supplychainai.pr.model.RequisitionItem;
import com.supplychainai.pr.repository.RequisitionItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requisition-items")
public class RequisitionItemController {

    private final RequisitionItemRepository repository;

    public RequisitionItemController(RequisitionItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<RequisitionItem>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequisitionItem> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RequisitionItem not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<RequisitionItem> create(@RequestBody RequisitionItem entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequisitionItem> update(@PathVariable UUID id, @RequestBody RequisitionItem entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
