package com.supplychainai.route.controller;

import com.supplychainai.route.model.RouteStop;
import com.supplychainai.route.repository.RouteStopRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/route-stops")
public class RouteStopController {

    private final RouteStopRepository repository;

    public RouteStopController(RouteStopRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<RouteStop>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteStop> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RouteStop not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<RouteStop> create(@RequestBody RouteStop entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteStop> update(@PathVariable UUID id, @RequestBody RouteStop entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
