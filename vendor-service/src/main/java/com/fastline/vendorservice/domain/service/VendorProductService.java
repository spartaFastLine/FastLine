package com.fastline.vendorservice.domain.service;

import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorProductService {

	private final ProductRepository productRepository;

	public List<Product> findProductInVendor(UUID vendorId, Pageable pageable) {
		return productRepository.findAllByVendorId(vendorId, pageable);
	}
}
