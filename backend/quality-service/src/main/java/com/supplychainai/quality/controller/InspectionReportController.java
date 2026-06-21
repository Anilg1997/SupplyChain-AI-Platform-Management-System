package com.supplychainai.quality.controller;

import com.supplychainai.quality.model.InspectionReport;
import com.supplychainai.quality.repository.InspectionReportRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inspection-reports")
public class InspectionReportController {

    private final InspectionReportRepository repository;

    public InspectionReportController(InspectionReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<InspectionReport>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionReport> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("InspectionReport not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<InspectionReport> create(@RequestBody InspectionReport entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionReport> update(@PathVariable UUID id, @RequestBody InspectionReport entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
