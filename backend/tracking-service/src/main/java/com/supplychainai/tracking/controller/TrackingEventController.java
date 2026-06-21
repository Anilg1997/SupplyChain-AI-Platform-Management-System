package com.supplychainai.tracking.controller;

import com.supplychainai.tracking.model.TrackingEvent;
import com.supplychainai.tracking.repository.TrackingEventRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking-events")
public class TrackingEventController {

    private final TrackingEventRepository repository;

    public TrackingEventController(TrackingEventRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<TrackingEvent>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackingEvent> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrackingEvent not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<TrackingEvent> create(@RequestBody TrackingEvent entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackingEvent> update(@PathVariable UUID id, @RequestBody TrackingEvent entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
