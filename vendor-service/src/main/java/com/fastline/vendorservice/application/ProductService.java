package com.fastline.vendorservice.application;

import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import com.fastline.vendorservice.domain.vo.Stock;
import com.fastline.vendorservice.presentation.request.ProductCreateRequest;
import com.fastline.vendorservice.presentation.request.ProductUpdateRequest;
import java.util.UUID;
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
	public Product insert(ProductCreateRequest request) {

		Vendor vendor = vendorService.findByVendorId(request.vendorId());
		//        hubClient.findHub();

		Product product =
				Product.create(request.name(), Stock.of(request.stock()), request.price(), vendor);

		return repository.insert(product);
	}

	@Transactional(readOnly = true)
	public Product findByProductId(UUID productId) {
		return repository.findByProductId(productId);
	}

	public Product updateProduct(ProductUpdateRequest request, UUID productId) {

		Product product = repository.findByProductId(productId);
		product.update(request.name(), request.stock(), request.price());

		return repository.insert(product);
	}

	public UUID deleteProduct(UUID productId) {
		return repository.deleteByProductId(productId);
	}
}
