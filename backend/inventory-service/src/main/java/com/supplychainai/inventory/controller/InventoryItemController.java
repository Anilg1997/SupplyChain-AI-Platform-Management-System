package com.supplychainai.inventory.controller;

import com.supplychainai.inventory.model.InventoryItem;
import com.supplychainai.inventory.repository.InventoryItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-items")
public class InventoryItemController {

    private final InventoryItemRepository repository;

    public InventoryItemController(InventoryItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItem>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("InventoryItem not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<InventoryItem> create(@RequestBody InventoryItem entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> update(@PathVariable UUID id, @RequestBody InventoryItem entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
