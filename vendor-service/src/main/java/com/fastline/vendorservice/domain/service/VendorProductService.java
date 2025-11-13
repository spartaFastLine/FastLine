package com.fastline.vendorservice.domain.service;

import com.fastline.vendorservice.domain.model.Product;
import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorProductService {

	private final ProductRepository productRepository;
	private final VendorRepository vendorRepository;

	public List<Product> findProductInVendor(UUID vendorId, Pageable pageable) {
		return productRepository.findAllByVendorId(vendorId, pageable);
	}

	public Vendor findVendorByVendorId(UUID vendorID) {
		return vendorRepository.findByVendorId(vendorID);
	}
}
