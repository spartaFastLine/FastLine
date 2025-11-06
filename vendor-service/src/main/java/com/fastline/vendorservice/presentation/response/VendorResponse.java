package com.fastline.vendorservice.presentation.response;

import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.vo.VendorType;
import com.fastline.vendorservice.domain.vo.VendorAddress;

import java.util.UUID;

public record VendorResponse(
        UUID vendorId,
        String vendorName,
        VendorType vendorType,
        VendorAddress address,
        UUID hubId
) {

    public static VendorResponse fromVendor(Vendor vendor) {
        return new VendorResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getType(),
                vendor.getAddress(),
                vendor.getHubId()
        );
    }
}
