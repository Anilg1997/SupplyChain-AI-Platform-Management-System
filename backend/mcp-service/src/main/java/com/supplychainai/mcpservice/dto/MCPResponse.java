package com.supplychainai.mcpservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MCPResponse {
    private boolean success;
    private Object result;
    private String error;
    private String tool;
    private String server;
    private long durationMs;
    private Instant timestamp;

    public MCPResponse() {
        this.timestamp = Instant.now();
    }

    public static MCPResponse ok(Object result, String tool, String server, long durationMs) {
        MCPResponse r = new MCPResponse();
        r.success = true;
        r.result = result;
        r.tool = tool;
        r.server = server;
        r.durationMs = durationMs;
        return r;
    }

    public static MCPResponse error(String error, String tool, String server) {
        MCPResponse r = new MCPResponse();
        r.success = false;
        r.error = error;
        r.tool = tool;
        r.server = server;
        return r;
    }

    public boolean isSuccess() { return success; }
    public Object getResult() { return result; }
    public String getError() { return error; }
    public String getTool() { return tool; }
    public String getServer() { return server; }
    public long getDurationMs() { return durationMs; }
    public Instant getTimestamp() { return timestamp; }
}
