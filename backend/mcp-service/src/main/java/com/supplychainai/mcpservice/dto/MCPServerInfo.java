package com.supplychainai.mcpservice.dto;

import java.util.List;
import java.util.Map;

public class MCPServerInfo {
    private String name;
    private boolean enabled;
    private String status;
    private List<String> tools;
    private Map<String, String> config;

    public MCPServerInfo(String name, boolean enabled, String status, List<String> tools) {
        this.name = name;
        this.enabled = enabled;
        this.status = status;
        this.tools = tools;
    }

    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }
    public String getStatus() { return status; }
    public List<String> getTools() { return tools; }
    public Map<String, String> getConfig() { return config; }
    public void setConfig(Map<String, String> config) { this.config = config; }
}
