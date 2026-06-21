package com.supplychainai.search.controller;

import com.supplychainai.search.model.SearchIndex;
import com.supplychainai.search.repository.SearchIndexRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/search-indexs")
public class SearchIndexController {

    private final SearchIndexRepository repository;

    public SearchIndexController(SearchIndexRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<SearchIndex>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchIndex> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SearchIndex not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<SearchIndex> create(@RequestBody SearchIndex entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SearchIndex> update(@PathVariable UUID id, @RequestBody SearchIndex entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
