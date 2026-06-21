package com.supplychainai.report.controller;

import com.supplychainai.report.model.ReportDefinition;
import com.supplychainai.report.repository.ReportDefinitionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report-definitions")
public class ReportDefinitionController {

    private final ReportDefinitionRepository repository;

    public ReportDefinitionController(ReportDefinitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ReportDefinition>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDefinition> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportDefinition not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<ReportDefinition> create(@RequestBody ReportDefinition entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDefinition> update(@PathVariable UUID id, @RequestBody ReportDefinition entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
