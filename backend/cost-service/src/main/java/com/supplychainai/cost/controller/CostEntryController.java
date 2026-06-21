package com.supplychainai.cost.controller;

import com.supplychainai.cost.model.CostEntry;
import com.supplychainai.cost.repository.CostEntryRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cost-entrys")
public class CostEntryController {

    private final CostEntryRepository repository;

    public CostEntryController(CostEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<CostEntry>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostEntry> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CostEntry not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<CostEntry> create(@RequestBody CostEntry entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostEntry> update(@PathVariable UUID id, @RequestBody CostEntry entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
