package com.supplychainai.quarantine.controller;

import com.supplychainai.quarantine.model.QuarantineOrder;
import com.supplychainai.quarantine.repository.QuarantineOrderRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quarantine-orders")
public class QuarantineOrderController {

    private final QuarantineOrderRepository repository;

    public QuarantineOrderController(QuarantineOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<QuarantineOrder>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuarantineOrder> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuarantineOrder not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<QuarantineOrder> create(@RequestBody QuarantineOrder entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuarantineOrder> update(@PathVariable UUID id, @RequestBody QuarantineOrder entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
