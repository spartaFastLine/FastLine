package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Vendor;

import java.util.UUID;

public interface VendorRepository {

    Vendor insert(Vendor vendor);

    Vendor findByVendorId(UUID vendorId);

    UUID deleteByVendorId(UUID vendorId);
}
