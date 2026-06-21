package com.supplychainai.pr.controller;

import com.supplychainai.pr.model.PurchaseRequisition;
import com.supplychainai.pr.repository.PurchaseRequisitionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-requisitions")
public class PurchaseRequisitionController {

    private final PurchaseRequisitionRepository repository;

    public PurchaseRequisitionController(PurchaseRequisitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseRequisition>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseRequisition not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<PurchaseRequisition> create(@RequestBody PurchaseRequisition entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> update(@PathVariable UUID id, @RequestBody PurchaseRequisition entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
