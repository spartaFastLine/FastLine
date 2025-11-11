package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

	Page<Product> findAllByVendorId(@Param("vendorId") UUID vendorId, Pageable pageable);
}
