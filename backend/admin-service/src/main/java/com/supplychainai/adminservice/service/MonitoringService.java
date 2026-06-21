package com.supplychainai.adminservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.management.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MonitoringService {

    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);

    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("service", "admin-service");
        health.put("status", "UP");

        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> jvm = new LinkedHashMap<>();
        jvm.put("availableProcessors", runtime.availableProcessors());
        jvm.put("freeMemory", runtime.freeMemory());
        jvm.put("totalMemory", runtime.totalMemory());
        jvm.put("maxMemory", runtime.maxMemory());
        jvm.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        jvm.put("memoryUsagePercent", String.format("%.1f",
            (double)(runtime.totalMemory() - runtime.freeMemory()) / runtime.totalMemory() * 100));
        health.put("jvm", jvm);

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> os = new LinkedHashMap<>();
        os.put("name", osBean.getName());
        os.put("version", osBean.getVersion());
        os.put("arch", osBean.getArch());
        os.put("availableProcessors", osBean.getAvailableProcessors());
        os.put("systemLoadAverage", osBean.getSystemLoadAverage());
        health.put("os", os);

        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Map<String, Object> threads = new LinkedHashMap<>();
        threads.put("liveThreads", threadBean.getThreadCount());
        threads.put("peakThreads", threadBean.getPeakThreadCount());
        threads.put("daemonThreads", threadBean.getDaemonThreadCount());
        health.put("threads", threads);

        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        Map<String, Object> heap = new LinkedHashMap<>();
        heap.put("init", memBean.getHeapMemoryUsage().getInit());
        heap.put("used", memBean.getHeapMemoryUsage().getUsed());
        heap.put("committed", memBean.getHeapMemoryUsage().getCommitted());
        heap.put("max", memBean.getHeapMemoryUsage().getMax());
        health.put("heap", heap);

        Map<String, Object> disk = new LinkedHashMap<>();
        for (File root : File.listRoots()) {
            disk.put(root.getAbsolutePath(), Map.of(
                "total", root.getTotalSpace(),
                "free", root.getFreeSpace(),
                "usable", root.getUsableSpace()
            ));
        }
        health.put("disk", disk);

        return health;
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("timestamp", LocalDateTime.now().toString());
        metrics.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());

        ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
        metrics.put("loadedClasses", classBean.getTotalLoadedClassCount());
        metrics.put("unloadedClasses", classBean.getUnloadedClassCount());

        MemoryPoolMXBean metaspaceBean = ManagementFactory.getMemoryPoolMXBeans().stream()
            .filter(p -> p.getName().contains("Metaspace"))
            .findFirst().orElse(null);
        if (metaspaceBean != null) {
            metrics.put("metaspace", Map.of(
                "used", metaspaceBean.getUsage().getUsed(),
                "max", metaspaceBean.getUsage().getMax()
            ));
        }

        return metrics;
    }

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("userDir", System.getProperty("user.dir"));
        info.put("timezone", TimeZone.getDefault().getDisplayName());
        info.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        return info;
    }
}
