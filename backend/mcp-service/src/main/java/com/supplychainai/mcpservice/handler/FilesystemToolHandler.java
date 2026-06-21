package com.supplychainai.mcpservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FilesystemToolHandler implements MCPToolHandler {

    private static final Logger log = LoggerFactory.getLogger(FilesystemToolHandler.class);

    @Value("${mcp.servers.filesystem:true}")
    private boolean enabled;

    @Value("${mcp.filesystem.workspace:/workspace}")
    private String workspacePath;

    @Override
    public String getServerName() { return "filesystem"; }

    @Override
    public List<String> getAvailableTools() {
        return List.of("read_file", "write_file", "list_directory", "file_info", "search_files", "delete_file", "copy_file", "move_file");
    }

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public Object executeTool(String tool, Map<String, Object> parameters) {
        Path basePath = Paths.get(workspacePath).toAbsolutePath().normalize();
        return switch (tool) {
            case "read_file" -> readFile(basePath, parameters);
            case "write_file" -> writeFile(basePath, parameters);
            case "list_directory" -> listDirectory(basePath, parameters);
            case "file_info" -> fileInfo(basePath, parameters);
            case "search_files" -> searchFiles(basePath, parameters);
            case "delete_file" -> deleteFile(basePath, parameters);
            case "copy_file" -> copyFile(basePath, parameters);
            case "move_file" -> moveFile(basePath, parameters);
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    private Path resolveAndValidate(Path basePath, String pathStr) {
        Path resolved = basePath.resolve(pathStr).normalize();
        if (!resolved.startsWith(basePath)) {
            throw new SecurityException("Path traversal denied: " + pathStr);
        }
        return resolved;
    }

    private Map<String, Object> readFile(Path basePath, Map<String, Object> params) {
        String path = (String) params.get("path");
        Path resolved = resolveAndValidate(basePath, path);
        try {
            String content = Files.readString(resolved);
            return Map.of("path", path, "content", content, "size", Files.size(resolved));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    private Map<String, Object> writeFile(Path basePath, Map<String, Object> params) {
        String path = (String) params.get("path");
        String content = (String) params.get("content");
        Path resolved = resolveAndValidate(basePath, path);
        try {
            Files.createDirectories(resolved.getParent());
            Files.writeString(resolved, content);
            return Map.of("path", path, "written", true, "size", content.length());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    private List<Map<String, Object>> listDirectory(Path basePath, Map<String, Object> params) {
        String path = (String) params.getOrDefault("path", ".");
        Path resolved = resolveAndValidate(basePath, path);
        try (Stream<Path> stream = Files.list(resolved)) {
            return stream.map(p -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);
                    return Map.of(
                        "name", p.getFileName().toString(),
                        "type", attrs.isDirectory() ? "directory" : "file",
                        "size", attrs.size(),
                        "lastModified", attrs.lastModifiedTime().toString()
                    );
                } catch (IOException e) {
                    return Map.of("name", p.getFileName().toString(), "type", "unknown");
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list directory: " + path, e);
        }
    }

    private Map<String, Object> fileInfo(Path basePath, Map<String, Object> params) {
        String path = (String) params.get("path");
        Path resolved = resolveAndValidate(basePath, path);
        try {
            BasicFileAttributes attrs = Files.readAttributes(resolved, BasicFileAttributes.class);
            Map<String, Object> info = new HashMap<>();
            info.put("path", path);
            info.put("exists", true);
            info.put("type", attrs.isDirectory() ? "directory" : "file");
            info.put("size", attrs.size());
            info.put("created", attrs.creationTime().toString());
            info.put("lastModified", attrs.lastModifiedTime().toString());
            info.put("lastAccessed", attrs.lastAccessTime().toString());
            info.put("readable", Files.isReadable(resolved));
            info.put("writable", Files.isWritable(resolved));
            info.put("executable", Files.isExecutable(resolved));
            return info;
        } catch (NoSuchFileException e) {
            return Map.of("path", path, "exists", false);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get file info: " + path, e);
        }
    }

    private List<String> searchFiles(Path basePath, Map<String, Object> params) {
        String pattern = (String) params.get("pattern");
        String directory = (String) params.getOrDefault("directory", ".");
        Path resolved = resolveAndValidate(basePath, directory);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        try (Stream<Path> stream = Files.walk(resolved)) {
            return stream
                .filter(p -> matcher.matches(p.getFileName()))
                .map(p -> basePath.relativize(p).toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to search files", e);
        }
    }

    private Map<String, Object> deleteFile(Path basePath, Map<String, Object> params) {
        String path = (String) params.get("path");
        Path resolved = resolveAndValidate(basePath, path);
        try {
            boolean deleted = Files.deleteIfExists(resolved);
            return Map.of("path", path, "deleted", deleted);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }

    private Map<String, Object> copyFile(Path basePath, Map<String, Object> params) {
        String source = (String) params.get("source");
        String destination = (String) params.get("destination");
        Path resolvedSource = resolveAndValidate(basePath, source);
        Path resolvedDest = resolveAndValidate(basePath, destination);
        try {
            Files.createDirectories(resolvedDest.getParent());
            Files.copy(resolvedSource, resolvedDest, StandardCopyOption.REPLACE_EXISTING);
            return Map.of("source", source, "destination", destination, "success", true);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file", e);
        }
    }

    private Map<String, Object> moveFile(Path basePath, Map<String, Object> params) {
        String source = (String) params.get("source");
        String destination = (String) params.get("destination");
        Path resolvedSource = resolveAndValidate(basePath, source);
        Path resolvedDest = resolveAndValidate(basePath, destination);
        try {
            Files.createDirectories(resolvedDest.getParent());
            Files.move(resolvedSource, resolvedDest, StandardCopyOption.REPLACE_EXISTING);
            return Map.of("source", source, "destination", destination, "success", true);
        } catch (IOException e) {
            throw new RuntimeException("Failed to move file", e);
        }
    }
}
