package com.supplychainai.invoice.controller;

import com.supplychainai.invoice.model.InvoiceLineItem;
import com.supplychainai.invoice.repository.InvoiceLineItemRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoice-line-items")
public class InvoiceLineItemController {

    private final InvoiceLineItemRepository repository;

    public InvoiceLineItemController(InvoiceLineItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceLineItem>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceLineItem> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvoiceLineItem not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<InvoiceLineItem> create(@RequestBody InvoiceLineItem entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceLineItem> update(@PathVariable UUID id, @RequestBody InvoiceLineItem entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
