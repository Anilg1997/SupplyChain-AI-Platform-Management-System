package com.supplychainai.report.controller;

import com.supplychainai.report.model.ReportSchedule;
import com.supplychainai.report.repository.ReportScheduleRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report-schedules")
public class ReportScheduleController {

    private final ReportScheduleRepository repository;

    public ReportScheduleController(ReportScheduleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ReportSchedule>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportSchedule> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportSchedule not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<ReportSchedule> create(@RequestBody ReportSchedule entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportSchedule> update(@PathVariable UUID id, @RequestBody ReportSchedule entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
