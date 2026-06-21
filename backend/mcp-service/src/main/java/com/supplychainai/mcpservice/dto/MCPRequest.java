package com.supplychainai.mcpservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class MCPRequest {
    @NotBlank
    private String server;
    @NotBlank
    private String tool;
    private Map<String, Object> parameters;
    private String sessionId;

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }
    public String getTool() { return tool; }
    public void setTool(String tool) { this.tool = tool; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}
