package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.VendorService;
import com.fastline.vendorservice.domain.model.Order;
import com.fastline.vendorservice.domain.model.Product;
import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdResponse;
import com.fastline.vendorservice.infrastructure.swagger.VendorControllerSwagger;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController implements VendorControllerSwagger {

	private final VendorService service;

	@PostMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<VendorCreateResponse>> insertVendor(
			@RequestBody @Valid VendorCreateRequest createRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Vendor vendor = service.insert(createRequest, userDetails.getUserId());
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
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<VendorOrderResponse>> getVendorOrders(
			@PathVariable UUID vendorId,
			@PageableDefault(direction = Sort.Direction.ASC, sort = "vendorProducerId") Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		List<Order> orderInVendor =
				service.findOrdersInVendor(vendorId, pageable, userDetails.getUserId());
		List<VendorOrder> vendorOrders =
				orderInVendor.stream()
						.map(p -> new VendorOrder(p.getId(), p.getStatus(), p.getArrivalTime()))
						.toList();

		VendorOrderResponse response = new VendorOrderResponse(vendorId, vendorOrders);

		return ResponseUtil.successResponse(SuccessCode.VENDOR_FIND_SUCCESS, response);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER', 'VENDOR_MANAGER')")
	public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(
			@RequestBody @Valid VendorUpdateRequest updateRequest,
			@RequestParam UUID vendorId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		if (updateRequest == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Vendor vendor = service.updateVendor(vendorId, updateRequest, userDetails.getUserId());
		VendorUpdateResponse response =
				new VendorUpdateResponse(
						vendor.getName(), vendor.getType(), vendor.getAddress(), vendor.getHubId());

		return ResponseUtil.successResponse(SuccessCode.VENDOR_UPDATE_SUCCESS, response);
	}

	@DeleteMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<UUID>> deleteVendor(
			@RequestParam UUID vendorId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		UUID deletedId = service.deleteVendor(vendorId, userDetails.getUserId());
		return ResponseUtil.successResponse(SuccessCode.VENDOR_DELETE_SUCCESS, deletedId);
	}

	@GetMapping("/info")
	public ResponseEntity<ApiResponse<VendorHubIdResponse>> sendToDeliveryVendorInfos(
			@RequestParam UUID vendorSenderId, @RequestParam UUID vendorReceiverId) {

		List<UUID> vendorHubId = service.findHubIdInVendor(vendorSenderId, vendorReceiverId);
		VendorHubIdResponse response =
				new VendorHubIdResponse(vendorHubId.get(0).toString(), vendorHubId.get(1).toString());

		return ResponseUtil.successResponse(SuccessCode.VENDOR_INFO_FIND_SUCCESS, response);
	}
}
