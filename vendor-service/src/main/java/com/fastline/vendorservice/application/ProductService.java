package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.service.HubClient;
import com.fastline.vendorservice.application.service.UserClient;
import com.fastline.vendorservice.domain.model.Product;
import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import com.fastline.vendorservice.domain.service.VendorProductService;
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
	private final VendorProductService vendorProductService;

	private final HubClient hubClient;
	private final UserClient userClient;

	public Product insert(ProductCreateRequest request, Long userId) {

		Vendor vendor = vendorProductService.findVendorByVendorId(request.vendorId());
		hubClient.getHubInfo(vendor.getId());

		Product product =
				Product.create(request.name(), Stock.of(request.stock()), request.price(), vendor);

		UUID userHubId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId)
				&& !vendor.isHubManager(userHubId)
				&& !vendor.isVendorManager(userId)) {
			throw new CustomException(ErrorCode.PRODUCT_FORBIDDEN);
		}

		return repository.insert(product);
	}

	@Transactional(readOnly = true)
	public Product findByProductId(UUID productId) {
		return repository.findByProductId(productId);
	}

	public Product updateProduct(ProductUpdateRequest request, UUID productId, Long userId) {

		Product product = repository.findByProductIdFetchVendor(productId);
		Vendor vendor = product.getVendor();

		UUID userHubId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId)
				&& !vendor.isHubManager(userHubId)
				&& !vendor.isVendorManager(userId)) {
			throw new CustomException(ErrorCode.PRODUCT_FORBIDDEN);
		}

		product.update(request.name(), request.stock(), request.price());

		return repository.insert(product);
	}

	public UUID deleteProduct(UUID productId, Long userId) {

		Product product = repository.findByProductIdFetchVendor(productId);
		Vendor vendor = product.getVendor();

		UUID userHubId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId) && !vendor.isHubManager(userHubId)) {
			throw new CustomException(ErrorCode.PRODUCT_FORBIDDEN);
		}

		return repository.deleteByProductId(productId);
	}

	private boolean isMasterUser(Long userId) {
		return userId == 1L;
	}
}
