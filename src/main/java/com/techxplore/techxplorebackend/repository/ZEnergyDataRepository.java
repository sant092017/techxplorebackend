package com.techxplore.techxplorebackend.repository;


import com.techxplore.techxplorebackend.entity.EnergyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZEnergyDataRepository extends JpaRepository<EnergyData, Long> {
    //update region, powerUsage using id
    @Modifying
    @Transactional
    @Query("UPDATE EnergyData e SET e.region = :region, e.powerUsage = :powerUsage WHERE e.id = :id")
    int updateEnergyDatabyId(@Param("id") Long id, @Param("region") String region, @Param("powerUsage") BigDecimal powerUsage);

    //Fetch updated Energy data using id
    Optional<EnergyData> findById(Long id);

    //Fetch All Energy Data
    List<EnergyData> findAll();

}
