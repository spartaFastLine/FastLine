package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.model.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

	Product insert(Product product);

	Product findByProductId(UUID productId);

	Product findByProductIdFetchVendor(UUID productId);

	UUID deleteByProductId(UUID productId);

	List<Product> findAllById(List<UUID> productIds);

	List<Product> findAllByVendorId(UUID vendorId, Pageable pageable);
}
