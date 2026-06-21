package com.supplychainai.warehouse.controller;

import com.supplychainai.warehouse.model.WarehouseZone;
import com.supplychainai.warehouse.repository.WarehouseZoneRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse-zones")
public class WarehouseZoneController {

    private final WarehouseZoneRepository repository;

    public WarehouseZoneController(WarehouseZoneRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseZone>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseZone> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("WarehouseZone not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<WarehouseZone> create(@RequestBody WarehouseZone entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseZone> update(@PathVariable UUID id, @RequestBody WarehouseZone entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
