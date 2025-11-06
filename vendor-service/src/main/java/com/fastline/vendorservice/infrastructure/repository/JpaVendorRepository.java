package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

    boolean existsById(UUID vendorId);
}
