package com.techxplore.techxplorebackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "energy_data")
public class EnergyData {

    @Id
    @Column(name="id")
    private Long id;

    @Column(name = "region")
    private String region;

    @Column(name="power_usage")
    private BigDecimal powerUsage;

    @Column(name="usage_time")
    private LocalDateTime usageTime;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    // Default no-args constructor for JPA
    public EnergyData() {
    }

    public EnergyData(Long id, String region, BigDecimal powerUsage, LocalDateTime usageTime, LocalDateTime createdAt) {
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
