# SupplyChain AI Platform - E2E Test Plan

## 1. Infrastructure Tests

### 1.1 Docker Infrastructure
```bash
# Start all services
docker-compose up -d

# Verify all containers are running
docker-compose ps

# Expected: 40+ containers running
# Verify specific services:
docker ps | grep scai-
```

### 1.2 Service Discovery (Eureka)
```bash
# Check Eureka dashboard
curl http://localhost:8761/eureka/apps
# Expected: 34 microservices registered
```

### 1.3 Config Server
```bash
curl http://localhost:8888/ai-rag-service/docker
# Expected: Returns configuration with AI/LLM settings
```

## 2. AI Service Tests

### 2.1 AI RAG Service (Port 8109)
```bash
# Health check
curl http://localhost:8109/actuator/health
curl http://localhost:8109/swagger-ui/index.html

# Store a document chunk
curl -X POST http://localhost:8109/api/v1/rag/chunks \
  -H "Content-Type: application/json" \
  -d '{"documentId":"550e8400-e29b-41d4-a716-446655440000","chunkIndex":1,"content":"Supply chain management test document","metadata":"{\"type\":\"test\"}","source":"test-doc.pdf"}'

# Search similar chunks
curl "http://localhost:8109/api/v1/rag/search?queryEmbedding=0.1,0.2,0.3&maxResults=5"

# Get distinct sources
curl http://localhost:8109/api/v1/rag/sources
```

### 2.2 MCP Service (Port 8110)
```bash
# Health check
curl http://localhost:8110/actuator/health

# Check MCP status
curl http://localhost:8110/api/v1/mcp/status
```

### 2.3 Admin Service (Port 8111)
```bash
# Health check
curl http://localhost:8111/actuator/health

# System health
curl http://localhost:8111/api/v1/admin/system/health
```

## 3. Authentication Tests

### 3.1 Login Flow
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Expected: JWT token response
```

### 3.2 Register Flow
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@supplychainai.com","password":"Test@123"}'
```

## 4. Domain Service Tests

### 4.1 Supplier Service
```bash
curl -X GET http://localhost:8083/suppliers \
  -H "Authorization: Bearer {TOKEN}"
```

### 4.2 Product Catalog
```bash
curl -X GET http://localhost:8088/api/products
```

### 4.3 Inventory Service
```bash
curl -X GET http://localhost:8089/api/inventory/stock-levels
```

### 4.4 Order Service
```bash
curl -X GET http://localhost:8091/api/orders
```

## 5. Frontend Tests

### 5.1 Angular App
```bash
cd frontend/supplychainai-ui
npm install
ng serve --port 4200

# Access: http://localhost:4200
# Verify:
# - Login page loads
# - Dashboard shows KPI cards
# - Sidebar navigation works
# - Admin panel accessible
```

### 5.2 AI RAG UI
- Navigate to "AI RAG Search" in sidebar
- Enter search query
- Verify results display
- Test document upload

## 6. AI/ML Pipeline Tests

### 6.1 Ollama LLM
```bash
# Verify Ollama is running
curl http://localhost:11434/api/tags

# Test model inference
curl -X POST http://localhost:11434/api/generate \
  -d '{"model":"llama3.1","prompt":"Hello","stream":false}'
```

### 6.2 PGVector
```bash
# Connect to PostgreSQL
docker exec -it scai-postgres psql -U postgres -d supplychainai

# Verify vector extension
\dT
SELECT * FROM pg_extension WHERE extname='vector';
```

## 7. Monitoring Tests

### 7.1 Prometheus
```bash
curl http://localhost:9090/api/v1/query?query=up
```

### 7.2 Grafana
```bash
# Access: http://localhost:3000 (admin/admin)
# Verify pre-configured dashboards load
```

## 8. API Gateway Tests

### 8.1 Gateway Routes
```bash
curl http://localhost:8080/api/v1/rag/search \
  -H "Authorization: Bearer {TOKEN}"
```

## Test Results

| Test | Status | Notes |
|------|--------|-------|
| Docker Compose | ⬜ | Run `docker-compose up -d` |
| Eureka Registry | ⬜ | 34 services should register |
| AI RAG Service | ⬜ | Verify document storage & search |
| MCP Service | ⬜ | Verify tool integration |
| Admin Service | ⬜ | Verify user & system management |
| Auth Service | ⬜ | JWT login/register flow |
| Supplier Service | ⬜ | CRUD operations |
| Product Catalog | ⬜ | MongoDB integration |
| Inventory Service | ⬜ | Stock levels & alerts |
| Ollama LLM | ⬜ | Model inference test |
| PGVector | ⬜ | Vector similarity search |
| Frontend UI | ⬜ | Angular SPA loads correctly |
| API Gateway | ⬜ | All routes functional |
| Monitoring | ⬜ | Prometheus/Grafana metrics |
