CREATE SCHEMA IF NOT EXISTS admin_schema;
CREATE TABLE IF NOT EXISTS admin_schema.admin_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE
);
CREATE TABLE IF NOT EXISTS admin_schema.system_config (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value TEXT,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE
);
INSERT INTO admin_schema.admin_users (username, email, role) VALUES
('admin', 'admin@supplychainai.com', 'SUPER_ADMIN')
ON CONFLICT (username) DO NOTHING;
INSERT INTO admin_schema.system_config (config_key, config_value, description) VALUES
('ai.rag.enabled', 'true', 'Enable RAG functionality'),
('ai.llm.model', 'llama3.1', 'Default LLM model'),
('ai.embedding.model', 'nomic-embed-text', 'Default embedding model'),
('system.maintenance.mode', 'false', 'System maintenance mode')
ON CONFLICT (config_key) DO NOTHING;
