package com.supplychainai.airagrservice.controller;

import com.supplychainai.airagrservice.model.DocumentChunk;
import com.supplychainai.airagrservice.service.RAGService;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rag")
public class RAGController {

    @Autowired
    private RAGService ragService;

    @PostMapping("/chunks")
    public ResponseEntity<DocumentChunk> storeDocumentChunk(@RequestBody DocumentChunk chunk) {
        DocumentChunk savedChunk = ragService.storeDocumentChunk(chunk);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChunk);
    }

    @GetMapping("/chunks/document/{documentId}")
    public ResponseEntity<List<DocumentChunk>> getDocumentChunks(@PathVariable UUID documentId) {
        List<DocumentChunk> chunks = ragService.getDocumentChunksByDocumentId(documentId);
        return ResponseEntity.ok(chunks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TextSegment>> searchSimilarChunks(
            @RequestParam String queryEmbedding,
            @RequestParam(required = false) String metadataFilters,
            @RequestParam(defaultValue = "5") int maxResults) {
        List<TextSegment> results = ragService.searchSimilarChunks(queryEmbedding, metadataFilters, maxResults);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/sources")
    public ResponseEntity<List<String>> getDistinctSources() {
        List<String> sources = ragService.getDistinctSources();
        return ResponseEntity.ok(sources);
    }

    @DeleteMapping("/chunks/document/{documentId}")
    public ResponseEntity<Void> deleteDocumentChunks(@PathVariable UUID documentId) {
        ragService.deleteDocumentChunks(documentId);
        return ResponseEntity.noContent().build();
    }
}