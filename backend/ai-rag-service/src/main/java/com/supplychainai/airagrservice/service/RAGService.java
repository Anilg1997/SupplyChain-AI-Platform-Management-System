package com.supplychainai.airagrservice.service;

import com.supplychainai.airagrservice.model.DocumentChunk;
import com.supplychainai.airagrservice.repository.DocumentChunkRepository;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.store.embedding.filter.MetadataFilter;
import dev.langchain4j.store.embedding.filter.MetadataFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RAGService {

    @Autowired
    private DocumentChunkRepository documentChunkRepository;

    @Autowired
    private PgVectorEmbeddingStore embeddingStore;

    public DocumentChunk storeDocumentChunk(DocumentChunk chunk) {
        return documentChunkRepository.save(chunk);
    }

    public List<DocumentChunk> getDocumentChunksByDocumentId(UUID documentId) {
        return documentChunkRepository.findByDocumentIdOrderByChunkIndex(documentId);
    }

    public List<TextSegment> searchSimilarChunks(String queryEmbedding, String metadataFilters, int maxResults) {
        MetadataFilter filter = parseMetadataFilters(metadataFilters);
        return embeddingStore.search(builder -> builder
            .filter(filter)
            .queryEmbedding(queryEmbedding)
            .maxResults(maxResults)
        );
    }

    public List<String> getDistinctSources() {
        return documentChunkRepository.findDistinctSources();
    }

    private MetadataFilter parseMetadataFilters(String metadataFilters) {
        if (metadataFilters == null || metadataFilters.isEmpty()) {
            return MetadataFilters.all();
        }
        return MetadataFilters.fromJson(metadataFilters);
    }

    public void deleteDocumentChunks(UUID documentId) {
        List<DocumentChunk> chunks = documentChunkRepository.findByDocumentIdOrderByChunkIndex(documentId);
        documentChunkRepository.deleteAll(chunks);
        embeddingStore.removeAll(chunks.stream()
            .map(chunk -> chunk.getId().toString())
            .toArray(String[]::new));
    }
}