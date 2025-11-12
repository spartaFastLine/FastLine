package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj;

import java.util.List;
import java.util.UUID;

public interface VendorRepository {

	Vendor insert(Vendor vendor);

	Vendor findByVendorId(UUID vendorId);

	UUID deleteByVendorId(UUID vendorId);

    List<VendorHubIdMappingObj> findVendorHubId(UUID vendorSenderId, UUID vendorReceiverId);
}
