package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.service.HubClient;
import com.fastline.vendorservice.application.service.UserClient;
import com.fastline.vendorservice.domain.model.Order;
import com.fastline.vendorservice.domain.model.Product;
import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.service.VendorOrderService;
import com.fastline.vendorservice.domain.service.VendorProductService;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj;
import com.fastline.vendorservice.presentation.request.VendorCreateRequest;
import com.fastline.vendorservice.presentation.request.VendorUpdateRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorService {

	private final VendorRepository repository;
	private final VendorProductService vendorProductService;
	private final VendorOrderService vendorOrderService;

	private final HubClient hubClient;
	private final UserClient userClient;

	public Vendor insert(VendorCreateRequest createRequest, Long userId) {

		VendorType vendorType = VendorType.fromString(createRequest.type());

		VendorAddress vendorAddress =
				VendorAddress.create(
						createRequest.city(),
						createRequest.district(),
						createRequest.roadName(),
						createRequest.zipCode());

		UUID passedHubId = validateHubId(createRequest.hubId());

		Vendor vendor =
				Vendor.create(createRequest.name(), vendorType, vendorAddress, passedHubId, userId);

		UUID hubManagerId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId) && vendor.isHubManager(hubManagerId)) {
			throw new CustomException(ErrorCode.VENDOR_FORBIDDEN);
		}

		return repository.insert(vendor);
	}

	@Transactional(readOnly = true)
	public Vendor findByVendorId(UUID vendorId) {

		return repository.findByVendorId(vendorId);
	}

	@Transactional(readOnly = true)
	public List<Product> findProductInVendor(UUID vendorId, Pageable pageable) {

		return vendorProductService.findProductInVendor(vendorId, pageable);
	}

	@Transactional(readOnly = true)
	public List<Order> findOrdersInVendor(UUID vendorId, Pageable pageable, Long userId) {

		Vendor vendor = findByVendorId(vendorId);

		UUID userHubId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId)
				&& !vendor.isHubManager(userHubId)
				&& !vendor.isVendorManager(userId)) {
			throw new CustomException(ErrorCode.VENDOR_FORBIDDEN);
		}

		return vendorOrderService.findOrdersInVendor(vendorId, pageable);
	}

	public Vendor updateVendor(UUID vendorId, VendorUpdateRequest updateRequest, Long userId) {

		Vendor vendor = repository.findByVendorId(vendorId);

		UUID hubUserId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId)
				&& !vendor.isHubManager(hubUserId)
				&& !vendor.isVendorManager(userId)) {
			throw new CustomException(ErrorCode.VENDOR_FORBIDDEN);
		}

		if (updateRequest.hubId() != null && updateRequest.hubId() != vendor.getHubId()) {
			validateHubId(updateRequest.hubId());
		}

		vendor.update(updateRequest);
		return repository.insert(vendor);
	}

	public UUID deleteVendor(UUID vendorId, Long userId) {

		Vendor vendor = repository.findByVendorId(vendorId);

		UUID userHubId = userClient.getUserHubId(userId);
		if (!isMasterUser(userId) && !vendor.isHubManager(userHubId)) {
			throw new CustomException(ErrorCode.VENDOR_FORBIDDEN);
		}

		return repository.deleteByVendorId(vendorId);
	}

	public List<UUID> findHubIdInVendor(UUID vendorSenderId, UUID vendorReceiverId) {
		Map<UUID, UUID> mappingId =
				repository.findHubIdInVendor(vendorSenderId, vendorReceiverId).stream()
						.collect(
								Collectors.toMap(VendorHubIdMappingObj::vendorID, VendorHubIdMappingObj::hubID));

		return List.of(mappingId.get(vendorSenderId), mappingId.get(vendorReceiverId));
	}

	private UUID validateHubId(UUID hubId) {

		/** 허브 서비스에서 예외처리를 구현한다면 리팩토링 필요 */
		hubClient.getHubInfo(hubId);
		return hubId;
	}

	private boolean isMasterUser(Long userId) {
		return userId == 1L;
	}
}
