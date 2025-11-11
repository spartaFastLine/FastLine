package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.VendorService;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.presentation.request.VendorCreateRequest;
import com.fastline.vendorservice.presentation.request.VendorUpdateRequest;
import com.fastline.vendorservice.presentation.response.vendor.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

	private final VendorService service;

	@PostMapping
	public ResponseEntity<ApiResponse<VendorCreateResponse>> insertVendor(
			@RequestBody @Valid VendorCreateRequest createRequest) {

		Vendor vendor = service.insert(createRequest);
		VendorCreateResponse response =
				new VendorCreateResponse(
						vendor.getId(),
						vendor.getName(),
						vendor.getType(),
						vendor.getAddress(),
						vendor.getHubId());

		return ResponseUtil.successResponse(SuccessCode.VENDOR_SAVE_SUCCESS, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<VendorFindResponse>> getVendor(@RequestParam UUID vendorId) {

		Vendor vendor = service.findByVendorId(vendorId);
		VendorFindResponse response =
				new VendorFindResponse(
						vendor.getId(),
						vendor.getName(),
						vendor.getType(),
						vendor.getAddress(),
						vendor.getHubId());

		return ResponseUtil.successResponse(SuccessCode.VENDOR_FIND_SUCCESS, response);
	}

	@GetMapping("/{vendorId}/products")
	public ResponseEntity<ApiResponse<VendorProductResponse>> getVendorProducts(
			@PathVariable UUID vendorId,
			@PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable) {

		List<Product> productInVendor = service.findProductInVendor(vendorId, pageable);
		List<VendorProduct> vendorProducts =
				productInVendor.stream()
						.map(p -> new VendorProduct(p.getId(), p.getStock(), p.getPrice()))
						.toList();

		VendorProductResponse response = new VendorProductResponse(vendorId, vendorProducts);

		return ResponseUtil.successResponse(SuccessCode.VENDOR_FIND_SUCCESS, response);
	}

	@GetMapping("/{vendorId}/orders")
	public ResponseEntity<ApiResponse<VendorOrderResponse>> getVendorOrders(
			@PathVariable UUID vendorId,
			@PageableDefault(direction = Sort.Direction.ASC, sort = "vendorProducerId")
					Pageable pageable) {

		List<Order> orderInVendor = service.findOrdersInVendor(vendorId, pageable);
		List<VendorOrder> vendorOrders =
				orderInVendor.stream()
						.map(p -> new VendorOrder(p.getId(), p.getStatus(), p.getArrivalTime()))
						.toList();

		VendorOrderResponse response = new VendorOrderResponse(vendorId, vendorOrders);

		return ResponseUtil.successResponse(SuccessCode.VENDOR_FIND_SUCCESS, response);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(
			@RequestBody @Valid VendorUpdateRequest updateRequest, @RequestParam UUID vendorId) {

		if (updateRequest == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Vendor vendor = service.updateVendor(vendorId, updateRequest);
		VendorUpdateResponse response =
				new VendorUpdateResponse(
						vendor.getName(), vendor.getType(), vendor.getAddress(), vendor.getHubId());

		return ResponseUtil.successResponse(SuccessCode.VENDOR_UPDATE_SUCCESS, response);
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<UUID>> deleteVendor(@RequestParam UUID vendorId) {

		UUID deletedId = service.deleteVendor(vendorId);
		return ResponseUtil.successResponse(SuccessCode.VENDOR_DELETE_SUCCESS, deletedId);
	}
}
