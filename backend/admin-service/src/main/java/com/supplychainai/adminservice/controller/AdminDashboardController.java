package com.supplychainai.adminservice.controller;

import com.supplychainai.adminservice.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminDashboardController {

    private final MonitoringService monitoringService;
    private final AdminUserService adminUserService;
    private final SystemConfigService systemConfigService;
    private final AIModelService aiModelService;

    public AdminDashboardController(MonitoringService monitoringService,
                                    AdminUserService adminUserService,
                                    SystemConfigService systemConfigService,
                                    AIModelService aiModelService) {
        this.monitoringService = monitoringService;
        this.adminUserService = adminUserService;
        this.systemConfigService = systemConfigService;
        this.aiModelService = aiModelService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("status", "UP");
        dashboard.put("timestamp", System.currentTimeMillis());

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalUsers", adminUserService.count());
        summary.put("configCount", systemConfigService.findAll().size());
        summary.put("aiModels", aiModelService.listModels().size());
        summary.put("jvmMemoryUsed", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        summary.put("jvmMemoryMax", Runtime.getRuntime().maxMemory());
        summary.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        dashboard.put("summary", summary);

        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(monitoringService.getSystemHealth());
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> metrics() {
        return ResponseEntity.ok(monitoringService.getMetrics());
    }

    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> systemInfo() {
        return ResponseEntity.ok(monitoringService.getSystemInfo());
    }
}
