package com.supplychainai.mcpservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GitToolHandler implements MCPToolHandler {

    private static final Logger log = LoggerFactory.getLogger(GitToolHandler.class);

    @Value("${mcp.servers.git:true}")
    private boolean enabled;

    @Value("${mcp.git.workspace:/workspace}")
    private String workspacePath;

    @Override
    public String getServerName() { return "git"; }

    @Override
    public List<String> getAvailableTools() {
        return List.of("status", "log", "diff", "branch", "commit", "push", "pull", "clone", "add", "checkout", "show");
    }

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public Object executeTool(String tool, Map<String, Object> parameters) {
        String repoPath = (String) parameters.getOrDefault("repoPath", ".");
        Path resolved = Paths.get(workspacePath).resolve(repoPath).normalize();
        return switch (tool) {
            case "status" -> gitStatus(resolved, parameters);
            case "log" -> gitLog(resolved, parameters);
            case "diff" -> gitDiff(resolved, parameters);
            case "branch" -> gitBranch(resolved, parameters);
            case "commit" -> gitCommit(resolved, parameters);
            case "add" -> gitAdd(resolved, parameters);
            case "checkout" -> gitCheckout(resolved, parameters);
            case "show" -> gitShow(resolved, parameters);
            case "clone" -> gitClone(parameters);
            case "push" -> gitPush(resolved, parameters);
            case "pull" -> gitPull(resolved, parameters);
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    private String execGit(Path repoPath, String... args) {
        List<String> cmd = new ArrayList<>(List.of("git"));
        cmd.addAll(List.of(args));
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(repoPath.toFile());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String output = new String(p.getInputStream().readAllBytes());
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Git command failed: " + output);
            }
            return output;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Git execution failed", e);
        }
    }

    private Map<String, Object> gitStatus(Path repoPath, Map<String, Object> params) {
        String output = execGit(repoPath, "status", "--porcelain");
        List<Map<String, String>> files = new ArrayList<>();
        for (String line : output.split("\n")) {
            if (line.isBlank()) continue;
            String status = line.substring(0, 2).trim();
            String file = line.substring(3).trim();
            files.add(Map.of("file", file, "status", status));
        }
        return Map.of("branch", getCurrentBranch(repoPath), "files", files, "clean", files.isEmpty());
    }

    private List<Map<String, Object>> gitLog(Path repoPath, Map<String, Object> params) {
        int count = (int) params.getOrDefault("count", 10);
        String output = execGit(repoPath, "log", "--oneline", "--decorate", "-" + count);
        return output.lines()
            .filter(l -> !l.isBlank())
            .map(l -> {
                String[] parts = l.split(" ", 2);
                Map<String, Object> entry = new HashMap<>();
                entry.put("hash", parts[0]);
                entry.put("message", parts.length > 1 ? parts[1] : "");
                return entry;
            })
            .collect(Collectors.toList());
    }

    private String gitDiff(Path repoPath, Map<String, Object> params) {
        String path = (String) params.get("path");
        if (path != null && !path.isBlank()) {
            return execGit(repoPath, "diff", "--", path);
        }
        return execGit(repoPath, "diff");
    }

    private List<String> gitBranch(Path repoPath, Map<String, Object> params) {
        String output = execGit(repoPath, "branch");
        return output.lines()
            .map(String::trim)
            .collect(Collectors.toList());
    }

    private Map<String, Object> gitCommit(Path repoPath, Map<String, Object> params) {
        String message = (String) params.get("message");
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Commit message is required");
        }
        String output = execGit(repoPath, "commit", "-m", message);
        return Map.of("message", output.trim(), "success", output.contains("changed") || output.contains("1 file"));
    }

    private Map<String, Object> gitAdd(Path repoPath, Map<String, Object> params) {
        String path = (String) params.getOrDefault("path", ".");
        String output = execGit(repoPath, "add", path);
        return Map.of("message", output.trim(), "success", true);
    }

    private Map<String, Object> gitCheckout(Path repoPath, Map<String, Object> params) {
        String branch = (String) params.get("branch");
        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Branch name is required");
        }
        boolean create = (boolean) params.getOrDefault("create", false);
        if (create) {
            execGit(repoPath, "checkout", "-b", branch);
        } else {
            execGit(repoPath, "checkout", branch);
        }
        return Map.of("branch", branch, "success", true);
    }

    private Map<String, Object> gitShow(Path repoPath, Map<String, Object> params) {
        String revision = (String) params.getOrDefault("revision", "HEAD");
        String output = execGit(repoPath, "show", "--stat", revision);
        return Map.of("revision", revision, "output", output);
    }

    private Map<String, Object> gitClone(Map<String, Object> params) {
        String url = (String) params.get("url");
        String directory = (String) params.getOrDefault("directory", "");
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Clone URL is required");
        }
        List<String> args = new ArrayList<>(List.of("clone", url));
        if (!directory.isBlank()) args.add(directory);
        String output = execGit(Paths.get(workspacePath), args.toArray(new String[0]));
        return Map.of("url", url, "output", output.trim(), "success", true);
    }

    private Map<String, Object> gitPush(Path repoPath, Map<String, Object> params) {
        String remote = (String) params.getOrDefault("remote", "origin");
        String branch = (String) params.getOrDefault("branch", "HEAD");
        String output = execGit(repoPath, "push", remote, branch);
        return Map.of("remote", remote, "branch", branch, "output", output.trim());
    }

    private Map<String, Object> gitPull(Path repoPath, Map<String, Object> params) {
        String remote = (String) params.getOrDefault("remote", "origin");
        String branch = (String) params.getOrDefault("branch", "");
        if (branch.isBlank()) {
            branch = getCurrentBranch(repoPath);
        }
        String output = execGit(repoPath, "pull", remote, branch);
        return Map.of("remote", remote, "branch", branch, "output", output.trim());
    }

    private String getCurrentBranch(Path repoPath) {
        return execGit(repoPath, "rev-parse", "--abbrev-ref", "HEAD").trim();
    }
}
