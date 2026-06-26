package com.techxplore.techxplorebackend.service;

import com.techxplore.techxplorebackend.controller.ZDataProcessController;
import com.techxplore.techxplorebackend.entity.EnergyData;
import com.techxplore.techxplorebackend.model.ZEnergyDataUpdateRequest;
import com.techxplore.techxplorebackend.model.ZEnergyDataUpdateResponse;
import com.techxplore.techxplorebackend.repository.ZEnergyDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZEnergyDataService {
    private static final Logger logger= LoggerFactory.getLogger(ZDataProcessController.class);

    @Autowired
    ZEnergyDataRepository zEnergyDataRepository;

    public ZEnergyDataUpdateResponse updateAndFetch(ZEnergyDataUpdateRequest zEnergyDataUpdateRequest) {
        logger.info("ZEnergyDataService | updateAndFetch | Updating and fetching energy data for request: {}", zEnergyDataUpdateRequest.getId());

        Long id= zEnergyDataUpdateRequest.getId();
        String region=zEnergyDataUpdateRequest.getRegion();
        BigDecimal powerUsage=zEnergyDataUpdateRequest.getPowerUsage();

        // Pass BigDecimal directly to repository (no conversion needed)
        int rowUpdated = zEnergyDataRepository.updateEnergyDatabyId(id, region, powerUsage);
        if (rowUpdated == 0) {
            logger.warn("ZEnergyDataService | updateEnergyDatabyId | No rows updated for request: {}", zEnergyDataUpdateRequest.getId());
            throw new RuntimeException("Update operation failed for id: " + id);
        }else {
            logger.info("ZEnergyDataService | updateEnergyDatabyId | Successfully updated energy data for request: {}", zEnergyDataUpdateRequest.getId());
        }

        //Fetch updated EnergyData from DB
        EnergyData updatedData = zEnergyDataRepository.findById(id).orElseThrow(() -> new RuntimeException("Energy data not found for id: " + id));

        return mapToResponse(updatedData);
    }

    //Fetch All EnergyData from DB
    public List<ZEnergyDataUpdateResponse> fetchAllEnergyData() {
        try {
            List<EnergyData> energyDataList = zEnergyDataRepository.findAll();
            logger.info("ZEnergyDataService | fetchAllEnergyData | Retrieved {} records from database", energyDataList.size());
            // Return empty list if no data found (REST best practice: 200 OK with empty array)
            return energyDataList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("ZEnergyDataService | fetchAllEnergyData | Error fetching data: ", e);
            throw new RuntimeException("Failed to fetch energy data: " + e.getMessage(), e);
        }
    }

    private ZEnergyDataUpdateResponse mapToResponse(EnergyData energyData) {
        BigDecimal powerUsage = (energyData.getPowerUsage() != null) ? energyData.getPowerUsage() : BigDecimal.ZERO;

        return new ZEnergyDataUpdateResponse(
                energyData.getId(),
                energyData.getRegion(),
                powerUsage,
                energyData.getUsageTime(),
                energyData.getCreatedAt()
        );
    }
}
