package com.supplychainai.quality.controller;

import com.supplychainai.quality.model.QualityCheck;
import com.supplychainai.quality.repository.QualityCheckRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quality-checks")
public class QualityCheckController {

    private final QualityCheckRepository repository;

    public QualityCheckController(QualityCheckRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<QualityCheck>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityCheck> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QualityCheck not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<QualityCheck> create(@RequestBody QualityCheck entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualityCheck> update(@PathVariable UUID id, @RequestBody QualityCheck entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
