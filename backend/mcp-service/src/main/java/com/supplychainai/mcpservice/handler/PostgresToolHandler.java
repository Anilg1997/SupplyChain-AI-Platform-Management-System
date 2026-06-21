package com.supplychainai.mcpservice.handler;

import com.supplychainai.mcpservice.config.MCPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PostgresToolHandler implements MCPToolHandler {

    private static final Logger log = LoggerFactory.getLogger(PostgresToolHandler.class);

    private final MCPConfig config;

    public PostgresToolHandler(MCPConfig config) {
        this.config = config;
    }

    @Override
    public String getServerName() { return "postgres"; }

    @Override
    public List<String> getAvailableTools() {
        return List.of("query", "list_tables", "describe_table", "list_schemas", "run_transaction");
    }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Object executeTool(String tool, Map<String, Object> parameters) {
        return switch (tool) {
            case "query" -> executeQuery(parameters);
            case "list_tables" -> listTables(parameters);
            case "describe_table" -> describeTable(parameters);
            case "list_schemas" -> listSchemas();
            case "run_transaction" -> runTransaction(parameters);
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    private Connection getConnection() throws SQLException {
        String url = config.getDatasourceUrl();
        String user = config.getDatasourceUsername();
        String pass = config.getDatasourcePassword();
        return DriverManager.getConnection(url, user, pass);
    }

    private Map<String, Object> executeQuery(Map<String, Object> params) {
        String sql = (String) params.get("sql");
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL query is required");
        }
        sql = sql.trim().toLowerCase();
        if (sql.startsWith("drop") || sql.startsWith("truncate") || sql.startsWith("alter")) {
            throw new SecurityException("DDL operations are not allowed via MCP");
        }
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery((String) params.get("sql"))) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            List<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                rows.add(row);
            }
            return Map.of("columns", java.util.stream.IntStream.range(1, columnCount + 1)
                .mapToObj(i -> { try { return meta.getColumnLabel(i); } catch (SQLException e) { return ""; } })
                .collect(Collectors.toList()),
                "rows", rows, "rowCount", rows.size());
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + e.getMessage(), e);
        }
    }

    private List<String> listTables(Map<String, Object> params) {
        String schema = (String) params.getOrDefault("schema", "public");
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ? AND table_type = 'BASE TABLE' ORDER BY table_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ResultSet rs = ps.executeQuery();
            List<String> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list tables", e);
        }
    }

    private List<Map<String, Object>> describeTable(Map<String, Object> params) {
        String table = (String) params.get("table");
        if (table == null || table.isBlank()) {
            throw new IllegalArgumentException("Table name is required");
        }
        String sql = "SELECT column_name, data_type, is_nullable, column_default, character_maximum_length " +
                     "FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> columns = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> col = new LinkedHashMap<>();
                col.put("name", rs.getString("column_name"));
                col.put("type", rs.getString("data_type"));
                col.put("nullable", "YES".equals(rs.getString("is_nullable")));
                col.put("default", rs.getString("column_default"));
                col.put("maxLength", rs.getObject("character_maximum_length"));
                columns.add(col);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to describe table", e);
        }
    }

    private List<String> listSchemas() {
        String sql = "SELECT schema_name FROM information_schema.schemata ORDER BY schema_name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<String> schemas = new ArrayList<>();
            while (rs.next()) {
                schemas.add(rs.getString("schema_name"));
            }
            return schemas;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list schemas", e);
        }
    }

    private List<Map<String, Object>> runTransaction(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> statements = (List<String>) params.get("statements");
        if (statements == null || statements.isEmpty()) {
            throw new IllegalArgumentException("Statements list is required");
        }
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    boolean isResultSet = stmt.execute(sql);
                    if (isResultSet) {
                        try (ResultSet rs = stmt.getResultSet()) {
                            results.add(processResultSet(rs));
                        }
                    } else {
                        results.add(Map.of("updateCount", stmt.getUpdateCount()));
                    }
                }
            }
            conn.commit();
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> processResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return Map.of("columns",
            java.util.stream.IntStream.range(1, columnCount + 1)
                .mapToObj(i -> { try { return meta.getColumnLabel(i); } catch (SQLException e) { return ""; } })
                .collect(Collectors.toList()),
            "rows", rows, "rowCount", rows.size());
    }
}
