package com.techxplore.techxplorebackend.model;

import java.math.BigDecimal;

public class ZEnergyDataUpdateRequest {
    private Long id;
    private String region;
    private BigDecimal powerUsage;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public BigDecimal getPowerUsage() { return powerUsage; }
    public void setPowerUsage(BigDecimal powerUsage) { this.powerUsage = powerUsage; }
}
