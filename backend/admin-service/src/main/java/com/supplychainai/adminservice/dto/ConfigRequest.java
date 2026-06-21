package com.supplychainai.adminservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ConfigRequest {
    @NotBlank
    private String configValue;
    private String description;

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
