package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.model.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

	Page<Product> findAllByVendorId(@Param("vendorId") UUID vendorId, Pageable pageable);

	@Query("select p from Product p join fetch p.vendor where p.id = :productId")
	Product findByProductFetchVendor(@Param("productId") UUID productId);
}
