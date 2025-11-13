package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj;
import java.util.List;
import java.util.UUID;

public interface VendorRepository {

	Vendor insert(Vendor vendor);

	Vendor findByVendorId(UUID vendorId);

	UUID deleteByVendorId(UUID vendorId);

	List<VendorHubIdMappingObj> findHubIdInVendor(UUID vendorSenderId, UUID vendorReceiverId);

	List<Vendor> findVendorByUserIdAndConsumerId(Long hubId, UUID vendorConsumerId);

	List<Vendor> findAllByHubId(UUID hubId);
}
