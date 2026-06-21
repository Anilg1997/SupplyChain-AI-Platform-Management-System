package com.supplychainai.adminservice.service;

import com.supplychainai.adminservice.dto.ConfigRequest;
import com.supplychainai.adminservice.model.SystemConfig;
import com.supplychainai.adminservice.repository.SystemConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SystemConfigService {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigService.class);

    private final SystemConfigRepository repository;

    public SystemConfigService(SystemConfigRepository repository) {
        this.repository = repository;
    }

    public List<SystemConfig> findAll() {
        return repository.findAll();
    }

    public SystemConfig findByKey(String configKey) {
        return repository.findByConfigKey(configKey)
            .orElseThrow(() -> new RuntimeException("Config not found: " + configKey));
    }

    public Map<String, String> getAllAsMap() {
        return repository.findAll().stream()
            .collect(Collectors.toMap(SystemConfig::getConfigKey, SystemConfig::getConfigValue));
    }

    @Transactional
    public SystemConfig update(String configKey, ConfigRequest request) {
        SystemConfig config = findByKey(configKey);
        config.setConfigValue(request.getConfigValue());
        if (request.getDescription() != null) {
            config.setDescription(request.getDescription());
        }
        SystemConfig saved = repository.save(config);
        log.info("System config updated: {} = {}", configKey, request.getConfigValue());
        return saved;
    }

    @Transactional
    public SystemConfig create(String configKey, ConfigRequest request) {
        if (repository.existsByConfigKey(configKey)) {
            throw new RuntimeException("Config key already exists: " + configKey);
        }
        SystemConfig config = new SystemConfig();
        config.setConfigKey(configKey);
        config.setConfigValue(request.getConfigValue());
        config.setDescription(request.getDescription());
        SystemConfig saved = repository.save(config);
        log.info("System config created: {} = {}", configKey, request.getConfigValue());
        return saved;
    }

    @Transactional
    public void delete(String configKey) {
        SystemConfig config = findByKey(configKey);
        repository.delete(config);
        log.info("System config deleted: {}", configKey);
    }

    public String getValue(String configKey, String defaultValue) {
        return repository.findByConfigKey(configKey)
            .map(SystemConfig::getConfigValue)
            .orElse(defaultValue);
    }
}
