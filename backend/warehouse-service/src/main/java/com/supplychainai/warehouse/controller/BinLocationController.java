package com.supplychainai.warehouse.controller;

import com.supplychainai.warehouse.model.BinLocation;
import com.supplychainai.warehouse.repository.BinLocationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bin-locations")
public class BinLocationController {

    private final BinLocationRepository repository;

    public BinLocationController(BinLocationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<BinLocation>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BinLocation> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("BinLocation not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<BinLocation> create(@RequestBody BinLocation entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BinLocation> update(@PathVariable UUID id, @RequestBody BinLocation entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
