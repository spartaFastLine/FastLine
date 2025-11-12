package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.service.HubClient;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
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

	public Vendor insert(VendorCreateRequest createRequest) {

		VendorType vendorType = VendorType.fromString(createRequest.type());

		VendorAddress vendorAddress =
				VendorAddress.create(
						createRequest.city(),
						createRequest.district(),
						createRequest.roadName(),
						createRequest.zipCode());

        validateHubId(createRequest.hubId());

        Vendor vendor =
				Vendor.create(createRequest.name(), vendorType, vendorAddress, createRequest.hubId());

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
	public List<Order> findOrdersInVendor(UUID vendorId, Pageable pageable) {
		return vendorOrderService.findOrdersInVendor(vendorId, pageable);
	}

	public Vendor updateVendor(UUID vendorId, VendorUpdateRequest updateRequest) {

		Vendor findVendor = repository.findByVendorId(vendorId);
		if (updateRequest.hubId() != null && updateRequest.hubId() != findVendor.getHubId()) {
            validateHubId(updateRequest.hubId());
		}

		findVendor.update(updateRequest);
		return repository.insert(findVendor);
	}

	public UUID deleteVendor(UUID vendorId) {
		return repository.deleteByVendorId(vendorId);
	}

    public List<UUID> findVendorHubId(UUID vendorSenderId, UUID vendorReceiverId) {
        Map<UUID, UUID> mappingId = repository.findVendorHubId(vendorSenderId, vendorReceiverId).stream().collect(Collectors.toMap(VendorHubIdMappingObj::vendorID, VendorHubIdMappingObj::hubID));

        return List.of(mappingId.get(vendorSenderId), mappingId.get(vendorReceiverId));
    }

    private boolean validateHubId(UUID hubId) {

        if (hubClient.validateHubId(hubId)) {
            throw new CustomException(ErrorCode.VENDOR_HUBID_INVALIDATION);
        }
        return true;
    }
}
