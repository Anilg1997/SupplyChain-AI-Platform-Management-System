package com.supplychainai.po.controller;

import com.supplychainai.po.model.PurchaseOrderItem;
import com.supplychainai.po.repository.PurchaseOrderItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-order-items")
public class PurchaseOrderItemController {

    private final PurchaseOrderItemRepository repository;

    public PurchaseOrderItemController(PurchaseOrderItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderItem>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrderItem not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderItem> create(@RequestBody PurchaseOrderItem entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> update(@PathVariable UUID id, @RequestBody PurchaseOrderItem entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
