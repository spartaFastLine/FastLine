package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.ProductService;
import com.fastline.vendorservice.application.command.CreateProductCommand;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.presentation.request.ProductCreateRequest;
import com.fastline.vendorservice.presentation.response.product.ProductCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductCreateResponse>> insertProduct(
			@RequestBody @Valid ProductCreateRequest createRequest) {

		CreateProductCommand createCommand =
				new CreateProductCommand(
						createRequest.name(),
						createRequest.stock(),
						createRequest.price(),
						createRequest.vendorId());

		Product product = service.insert(createCommand);
		ProductCreateResponse response =
				new ProductCreateResponse(
						product.getId(), product.getStock(), product.getPrice(), product.getVendor().getId());
		return ResponseUtil.successResponse(SuccessCode.PRODUCT_SAVE_SUCCESS, response);
	}
}
