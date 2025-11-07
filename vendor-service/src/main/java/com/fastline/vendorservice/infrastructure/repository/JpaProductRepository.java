package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {}
