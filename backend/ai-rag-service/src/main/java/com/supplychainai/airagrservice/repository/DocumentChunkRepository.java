package com.supplychainai.airagrservice.repository;

import com.supplychainai.airagrservice.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(UUID documentId);

    @Query(value = """
        SELECT id, content, metadata, source, created_at, updated_at,
               1 - (embedding <=> ?2) as similarity
        FROM ai_schema.document_chunks
        WHERE metadata @> ?3::jsonb
        ORDER BY embedding <=> ?2
        LIMIT ?4
        """, nativeQuery = true)
    List<DocumentChunk> searchSimilarChunks(float[] embedding, String filters, int limit);

    @Query(value = """
        SELECT DISTINCT source FROM ai_schema.document_chunks
        """, nativeQuery = true)
    List<String> findDistinctSources();
}