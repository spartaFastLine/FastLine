package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Product;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {

	Product insert(Product product);

	Product findByProductId(UUID productId);

	UUID deleteByProductId(UUID productId);

	List<Product> findAllById(List<UUID> productIds);
}
