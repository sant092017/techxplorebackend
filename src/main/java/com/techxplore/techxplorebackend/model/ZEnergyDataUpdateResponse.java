package com.techxplore.techxplorebackend.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ZEnergyDataUpdateResponse {

    private Long id;
    private String region;
    private BigDecimal powerUsage;
    private LocalDateTime usageTime;
    private LocalDateTime createdAt;

    // Default no-args constructor
    public ZEnergyDataUpdateResponse() {
    }

    public ZEnergyDataUpdateResponse(Long id, String region, BigDecimal powerUsage, LocalDateTime usageTime, LocalDateTime createdAt) {
        this.id = id;
        this.region = region;
        this.powerUsage = powerUsage;
        this.usageTime = usageTime;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(BigDecimal powerUsage) {
        this.powerUsage = powerUsage;
    }

    public LocalDateTime getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(LocalDateTime usageTime) {
        this.usageTime = usageTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
