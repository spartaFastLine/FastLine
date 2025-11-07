package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {}
