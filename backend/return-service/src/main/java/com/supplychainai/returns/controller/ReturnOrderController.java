package com.supplychainai.returns.controller;

import com.supplychainai.returns.model.ReturnOrder;
import com.supplychainai.returns.repository.ReturnOrderRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/return-orders")
public class ReturnOrderController {

    private final ReturnOrderRepository repository;

    public ReturnOrderController(ReturnOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ReturnOrder>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnOrder> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReturnOrder not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<ReturnOrder> create(@RequestBody ReturnOrder entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnOrder> update(@PathVariable UUID id, @RequestBody ReturnOrder entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
