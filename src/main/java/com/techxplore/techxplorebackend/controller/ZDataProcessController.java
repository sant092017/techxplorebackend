package com.techxplore.techxplorebackend.controller;

import com.techxplore.techxplorebackend.model.ZEnergyDataUpdateRequest;
import com.techxplore.techxplorebackend.model.ZEnergyDataUpdateResponse;
import com.techxplore.techxplorebackend.service.ZEnergyDataService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/zdataprocess")
@Validated
public class ZDataProcessController {
    private static final Logger logger= LoggerFactory.getLogger(ZDataProcessController.class);

    @Autowired
    ZEnergyDataService zEnergyDataService;

    @PutMapping("/updateFetch")
    public ZEnergyDataUpdateResponse updateAndFetch(
            @Valid
            @RequestBody ZEnergyDataUpdateRequest zEnergyDataUpdateRequest) {
        ZEnergyDataUpdateResponse updatedFetchResponse = zEnergyDataService.updateAndFetch(zEnergyDataUpdateRequest);
        if(updatedFetchResponse == null) {
            logger.warn("Update and fetch operation FAILED for request: {}", zEnergyDataUpdateRequest.getId());
            throw new RuntimeException("Update and fetch operation failed");
        }else {
            logger.info("Update and fetch operation successful for request: {}", zEnergyDataUpdateRequest.getId());
        }
        return updatedFetchResponse;
    }

    @GetMapping("/fetchAllEnergyData")
    public List<ZEnergyDataUpdateResponse> fetchAllEnergyData() {
        List<ZEnergyDataUpdateResponse> listOfEnergyData = zEnergyDataService.fetchAllEnergyData();
        return listOfEnergyData;
    }
}



