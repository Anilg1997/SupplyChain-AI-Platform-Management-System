package com.supplychainai.rfq.controller;

import com.supplychainai.rfq.model.RFQ;
import com.supplychainai.rfq.repository.RFQRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rfqs")
public class RFQController {

    private final RFQRepository repository;

    public RFQController(RFQRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<RFQ>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RFQ> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RFQ not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<RFQ> create(@RequestBody RFQ entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RFQ> update(@PathVariable UUID id, @RequestBody RFQ entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
