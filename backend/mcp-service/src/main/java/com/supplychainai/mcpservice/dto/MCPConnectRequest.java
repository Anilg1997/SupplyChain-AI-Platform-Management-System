package com.supplychainai.mcpservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class MCPConnectRequest {
    @NotBlank
    private String server;
    private Map<String, String> config;

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }
    public Map<String, String> getConfig() { return config; }
    public void setConfig(Map<String, String> config) { this.config = config; }
}
