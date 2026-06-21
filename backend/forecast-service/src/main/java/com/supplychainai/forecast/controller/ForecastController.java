package com.supplychainai.forecast.controller;

import com.supplychainai.forecast.model.Forecast;
import com.supplychainai.forecast.repository.ForecastRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/forecasts")
public class ForecastController {

    private final ForecastRepository repository;

    public ForecastController(ForecastRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Forecast>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Forecast> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forecast not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<Forecast> create(@RequestBody Forecast entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Forecast> update(@PathVariable UUID id, @RequestBody Forecast entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
