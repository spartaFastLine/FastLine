package com.fastline.vendorservice.application;

import com.fastline.vendorservice.application.command.CreateProductCommand;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository repository;
	private final VendorService vendorService;

	//    private final HubClient hubClient

	/** TODO: vendor의 hubId가 유효한지 확인하는 흐름 작성 필요. */
	public Product insert(CreateProductCommand command) {

		Vendor vendor = vendorService.findByVendorId(command.vendorId());
		//        hubClient.findHub();

		Product product = Product.create(command.name(), command.stock(), command.price(), vendor);

		return repository.insert(product);
	}
}
