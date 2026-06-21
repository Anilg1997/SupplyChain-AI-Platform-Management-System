package com.supplychainai.supplier.controller;

import com.supplychainai.supplier.model.SupplierContact;
import com.supplychainai.supplier.repository.SupplierContactRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/supplier-contacts")
public class SupplierContactController {

    private final SupplierContactRepository repository;

    public SupplierContactController(SupplierContactRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<SupplierContact>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierContact> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierContact not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<SupplierContact> create(@RequestBody SupplierContact entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierContact> update(@PathVariable UUID id, @RequestBody SupplierContact entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
