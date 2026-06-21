package com.supplychainai.rfq.controller;

import com.supplychainai.rfq.model.RFQResponse;
import com.supplychainai.rfq.repository.RFQResponseRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rfqresponses")
public class RFQResponseController {

    private final RFQResponseRepository repository;

    public RFQResponseController(RFQResponseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<RFQResponse>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RFQResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RFQResponse not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<RFQResponse> create(@RequestBody RFQResponse entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RFQResponse> update(@PathVariable UUID id, @RequestBody RFQResponse entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
