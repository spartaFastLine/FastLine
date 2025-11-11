package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

	private final JpaProductRepository jpaProductRepository;

	@Override
	public Product insert(Product product) {

		Product insertedProduct = jpaProductRepository.save(product);

		try {
			jpaProductRepository.flush();
		} catch (DataIntegrityViolationException de) {
			throw new CustomException(ErrorCode.PRODUCT_NAME_DUPLICATED);
		}

		return insertedProduct;
	}

	@Override
	public Product findByProductId(UUID productId) {

		return jpaProductRepository
				.findById(productId)
				.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	@Override
	public List<Product> findAllById(List<UUID> productIds) {
		return jpaProductRepository.findAllById(productIds);
	}

	@Override
	public List<Product> findAllByVendorId(UUID vendorId, Pageable pageable) {
		Page<Product> pagedProduct = jpaProductRepository.findAllByVendorId(vendorId, pageable);
		return pagedProduct.getContent();
	}

	@Override
	public UUID deleteByProductId(UUID productId) {

		jpaProductRepository.deleteById(productId);
		return productId;
	}
}
