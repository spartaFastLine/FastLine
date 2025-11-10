package com.fastline.vendorservice;

import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DummyData")
public class VendorDummyDataInit implements ApplicationRunner {

	private final VendorRepository vendorRepository;
	private final ProductRepository productRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Vendor vendor =
				vendorRepository.insert(
						Vendor.create(
								"testVendor",
								VendorType.PRODUCER,
								VendorAddress.create("경기도", "분당구", "정자동", "12385"),
								UUID.randomUUID()));

		Product product = productRepository.insert(Product.create("testProduct", 1000, 5000.0, vendor));
		Product product1 =
				productRepository.insert(Product.create("testProduct1", 2000, 2000.0, vendor));
		log.info("vendorId = {}", vendor.getId());
		log.info("productId = {}", product.getId());
		log.info("product1Id = {}", product1.getId());
	}
}
