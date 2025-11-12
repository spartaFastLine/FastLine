package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Vendor;

import java.util.List;
import java.util.UUID;

import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

    @Query("select new com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj(v.id, v.hubId) from Vendor v where v.id in :vendorIds")
    List<VendorHubIdMappingObj> findVendorHubId(@Param("vendorIds") List<UUID> vendorIds);
}
