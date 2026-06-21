package com.supplychainai.mcpservice.handler;

import java.util.List;
import java.util.Map;

public interface MCPToolHandler {
    String getServerName();
    List<String> getAvailableTools();
    boolean isEnabled();
    Object executeTool(String tool, Map<String, Object> parameters);
}
