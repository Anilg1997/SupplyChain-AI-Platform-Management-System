package com.supplychainai.portal.controller;

import com.supplychainai.portal.model.PortalDocument;
import com.supplychainai.portal.repository.PortalDocumentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portal-documents")
public class PortalDocumentController {

    private final PortalDocumentRepository repository;

    public PortalDocumentController(PortalDocumentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PortalDocument>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortalDocument> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PortalDocument not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<PortalDocument> create(@RequestBody PortalDocument entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortalDocument> update(@PathVariable UUID id, @RequestBody PortalDocument entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
