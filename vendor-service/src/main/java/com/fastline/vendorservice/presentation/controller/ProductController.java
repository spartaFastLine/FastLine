package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.ProductService;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.presentation.request.ProductCreateRequest;
import com.fastline.vendorservice.presentation.request.ProductUpdateRequest;
import com.fastline.vendorservice.presentation.response.product.ProductCreateResponse;
import com.fastline.vendorservice.presentation.response.product.ProductFindResponse;
import com.fastline.vendorservice.presentation.response.product.ProductUpdateResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductCreateResponse>> insertProduct(
			@RequestBody @Valid ProductCreateRequest createRequest) {

		Product product = service.insert(createRequest);
		ProductCreateResponse response =
				new ProductCreateResponse(
						product.getId(), product.getStock(), product.getPrice(), product.getVendor().getId());
		return ResponseUtil.successResponse(SuccessCode.PRODUCT_SAVE_SUCCESS, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<ProductFindResponse>> findProduct(
			@RequestParam UUID productId) {

		Product product = service.findByProductId(productId);
		ProductFindResponse response =
				new ProductFindResponse(
						product.getId(), product.getStock(), product.getPrice(), product.getVendor().getId());
		return ResponseUtil.successResponse(SuccessCode.PRODUCT_FIND_SUCCESS, response);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
			@RequestBody @Valid ProductUpdateRequest updateRequest, @RequestParam UUID productId) {

		if (updateRequest == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Product product = service.updateProduct(updateRequest, productId);
		ProductUpdateResponse response =
				new ProductUpdateResponse(product.getName(), product.getStock(), product.getPrice());
		return ResponseUtil.successResponse(SuccessCode.PRODUCT_UPDATE_SUCCESS, response);
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<UUID>> deleteProduct(@RequestParam UUID productId) {

		UUID deletedId = service.deleteProduct(productId);
		return ResponseUtil.successResponse(SuccessCode.PRODUCT_DELETE_SUCCESS, deletedId);
	}
}
