-- AI/RAG Service Database Schema
-- Create AI schema
CREATE SCHEMA IF NOT EXISTS ai_schema;

-- Document chunks table for RAG
CREATE TABLE IF NOT EXISTS ai_schema.document_chunks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL,
    chunk_index INTEGER NOT NULL,
    content TEXT NOT NULL,
    embedding vector(768),
    metadata JSONB,
    source VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_document_chunks_document FOREIGN KEY (document_id) REFERENCES supplychainai.user_schema.users(id) ON DELETE CASCADE
);

-- Create vector extension if not exists
CREATE EXTENSION IF NOT EXISTS vector;

-- Create index for vector similarity search
CREATE INDEX IF NOT EXISTS idx_document_chunks_embedding ON ai_schema.document_chunks USING hnsw (embedding vector_cosine_ops);

-- Create index for metadata filtering
CREATE INDEX IF NOT EXISTS idx_document_chunks_metadata ON ai_schema.document_chunks USING gin (metadata);

-- Create index for document_id
CREATE INDEX IF NOT EXISTS idx_document_chunks_document_id ON ai_schema.document_chunks (document_id);

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_updated_at
BEFORE UPDATE ON ai_schema.document_chunks
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data for testing
INSERT INTO ai_schema.document_chunks (document_id, chunk_index, content, metadata, source)
VALUES 
    (gen_random_uuid(), 1, 'Supply chain management involves coordinating the flow of goods from suppliers to customers.', '{"type": "concept", "category": "supply-chain"}', 'supply-chain-basics.pdf'),
    (gen_random_uuid(), 2, 'Inventory optimization reduces holding costs while maintaining service levels.', '{"type": "strategy", "category": "inventory"}', 'inventory-optimization.pdf'),
    (gen_random_uuid(), 3, 'Demand forecasting uses historical data and market trends to predict future sales.', '{"type": "technique", "category": "forecasting"}', 'demand-forecasting.pdf'),
    (gen_random_uuid(), 4, 'Supplier relationship management builds partnerships for mutual benefit.', '{"type": "practice", "category": "supplier-management"}', 'supplier-relationships.pdf'),
    (gen_random_uuid(), 5, 'Warehouse automation improves efficiency and reduces errors in order processing.', '{"type": "technology", "category": "automation"}', 'warehouse-automation.pdf');

-- Create view for easy access
CREATE OR REPLACE VIEW ai_schema.document_chunks_view AS
SELECT 
    dc.id,
    dc.document_id,
    dc.chunk_index,
    dc.content,
    dc.metadata,
    dc.source,
    dc.created_at,
    dc.updated_at,
    (dc.embedding <=> '[0.1,0.2,0.3,0.4,0.5]') as similarity_score
FROM ai_schema.document_chunks dc
ORDER BY dc.created_at DESC;