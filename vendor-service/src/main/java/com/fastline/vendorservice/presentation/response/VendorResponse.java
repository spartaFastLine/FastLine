package com.fastline.vendorservice.presentation.response;

import com.fastline.vendorservice.domain.entity.VendorType;
import com.fastline.vendorservice.domain.vo.VendorAddress;

import java.util.UUID;

public record VendorResponse(
        UUID vendorId,
        String vendorName,
        VendorType vendorType,
        VendorAddress address,
        UUID hubId
) {
}
