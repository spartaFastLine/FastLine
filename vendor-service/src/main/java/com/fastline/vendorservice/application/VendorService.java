package com.fastline.vendorservice.application;

import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import com.fastline.vendorservice.presentation.request.VendorCreateRequest;
import com.fastline.vendorservice.presentation.request.VendorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorService {

	private final VendorRepository repository;

	//    private final HubClient hubClient; -- 허브 서비스로 API 호출을 보낼 책임을 갖음

	/**
	 * TODO: 허브 서비스로의 API 호출 성공, 실패시 흐름처리 작성 필요 TODO: 성공시, Vendor.create의 UUID.randomUUID() 부분에 허브ID.
	 * 실패시 적절한 예외처리
	 */
	public Vendor insert(VendorCreateRequest createRequest) {

		VendorType vendorType = VendorType.fromString(createRequest.type());

		VendorAddress vendorAddress =
				VendorAddress.create(
						createRequest.city(),
						createRequest.district(),
						createRequest.roadName(),
						createRequest.zipCode());

		//        hubClient.findHub(); -- 존재하는 허브인지 허브 서비스로 API 호출. 없거나 에러시 적절한 처리 필요

		Vendor vendor =
				Vendor.create(createRequest.name(), vendorType, vendorAddress, UUID.randomUUID());

		return repository.insert(vendor);
	}

	@Transactional(readOnly = true)
	public Vendor findByVendorId(UUID vendorId) {

		return repository.findByVendorId(vendorId);
	}

	/** TODO: hubId 업데이트 시도시, 유효한 Id인지 허브서비스로의 API요청 흐름 필요. TODO: 성공시 그대로 진행, 실패시 적절한 예외처리 */
	public Vendor updateVendor(UUID vendorId, VendorUpdateRequest updateRequest) {

		Vendor findVendor = repository.findByVendorId(vendorId);
		if (updateRequest.hubId() != null && updateRequest.hubId() != findVendor.getHubId()) {
			//            hubClient.findHub(); -- 존재하는 허브인지 허브 서비스로 API 호출. 없거나 에러시 적절한 처리 필요
		}

		findVendor.update(updateRequest);
		return repository.insert(findVendor);
	}

	public UUID deleteVendor(UUID vendorId) {
		return repository.deleteByVendorId(vendorId);
	}
}
