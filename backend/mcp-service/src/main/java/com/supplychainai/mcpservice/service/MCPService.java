package com.supplychainai.mcpservice.service;

import com.supplychainai.mcpservice.dto.MCPConnectRequest;
import com.supplychainai.mcpservice.dto.MCPRequest;
import com.supplychainai.mcpservice.dto.MCPResponse;
import com.supplychainai.mcpservice.dto.MCPServerInfo;
import com.supplychainai.mcpservice.handler.MCPToolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MCPService {

    private static final Logger log = LoggerFactory.getLogger(MCPService.class);

    private final List<MCPToolHandler> handlers;
    private final Map<String, Map<String, String>> serverConfigs = new ConcurrentHashMap<>();
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public MCPService(List<MCPToolHandler> handlers) {
        this.handlers = handlers;
    }

    public List<MCPServerInfo> getServers() {
        return handlers.stream().map(h -> {
            MCPServerInfo info = new MCPServerInfo(
                h.getServerName(),
                h.isEnabled(),
                h.isEnabled() ? "connected" : "disabled",
                h.getAvailableTools()
            );
            info.setConfig(serverConfigs.get(h.getServerName()));
            return info;
        }).collect(Collectors.toList());
    }

    public MCPResponse executeTool(MCPRequest request) {
        String server = request.getServer();
        String tool = request.getTool();
        MCPToolHandler handler = findHandler(server);
        if (handler == null) {
            return MCPResponse.error("Server not found: " + server, tool, server);
        }
        if (!handler.isEnabled()) {
            return MCPResponse.error("Server is disabled: " + server, tool, server);
        }
        if (!handler.getAvailableTools().contains(tool)) {
            return MCPResponse.error("Tool not available: " + tool + " on server: " + server, tool, server);
        }
        long start = System.currentTimeMillis();
        try {
            Object result = handler.executeTool(tool, request.getParameters());
            long duration = System.currentTimeMillis() - start;
            if (request.getSessionId() != null) {
                sessions.put(request.getSessionId(), server);
            }
            return MCPResponse.ok(result, tool, server, duration);
        } catch (Exception e) {
            log.error("MCP tool execution failed: {}/{} - {}", server, tool, e.getMessage());
            return MCPResponse.error(e.getMessage(), tool, server);
        }
    }

    public Map<String, Object> connect(MCPConnectRequest request) {
        String server = request.getServer();
        MCPToolHandler handler = findHandler(server);
        if (handler == null) {
            throw new IllegalArgumentException("Server not found: " + server);
        }
        if (!handler.isEnabled()) {
            throw new IllegalStateException("Server is disabled: " + server);
        }
        Map<String, String> config = request.getConfig();
        if (config != null) {
            serverConfigs.put(server, config);
        }
        log.info("MCP server connected: {}", server);
        return Map.of(
            "server", server,
            "status", "connected",
            "tools", handler.getAvailableTools(),
            "sessionId", UUID.randomUUID().toString()
        );
    }

    public Map<String, Object> getStatus() {
        long totalTools = handlers.stream()
            .filter(MCPToolHandler::isEnabled)
            .mapToLong(h -> h.getAvailableTools().size())
            .sum();
        return Map.of(
            "status", "running",
            "servers", handlers.size(),
            "enabledServers", handlers.stream().filter(MCPToolHandler::isEnabled).count(),
            "totalTools", totalTools,
            "activeSessions", sessions.size()
        );
    }

    private MCPToolHandler findHandler(String serverName) {
        return handlers.stream()
            .filter(h -> h.getServerName().equalsIgnoreCase(serverName))
            .findFirst()
            .orElse(null);
    }
}
