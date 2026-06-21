package com.supplychainai.mcpservice.controller;

import com.supplychainai.mcpservice.dto.*;
import com.supplychainai.mcpservice.service.MCPService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mcp")
public class MCPController {

    private final MCPService mcpService;

    public MCPController(MCPService mcpService) {
        this.mcpService = mcpService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(mcpService.getStatus());
    }

    @GetMapping("/servers")
    public ResponseEntity<List<MCPServerInfo>> getServers() {
        return ResponseEntity.ok(mcpService.getServers());
    }

    @PostMapping("/connect")
    public ResponseEntity<Map<String, Object>> connect(@Valid @RequestBody MCPConnectRequest request) {
        return ResponseEntity.ok(mcpService.connect(request));
    }

    @PostMapping("/execute")
    public ResponseEntity<MCPResponse> execute(@Valid @RequestBody MCPRequest request) {
        return ResponseEntity.ok(mcpService.executeTool(request));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "mcp-service",
            "servers", mcpService.getServers().size()
        ));
    }
}
