package com.supplychainai.tracking.controller;

import com.supplychainai.tracking.model.TrackingAlert;
import com.supplychainai.tracking.repository.TrackingAlertRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking-alerts")
public class TrackingAlertController {

    private final TrackingAlertRepository repository;

    public TrackingAlertController(TrackingAlertRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<TrackingAlert>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackingAlert> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrackingAlert not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<TrackingAlert> create(@RequestBody TrackingAlert entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackingAlert> update(@PathVariable UUID id, @RequestBody TrackingAlert entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
