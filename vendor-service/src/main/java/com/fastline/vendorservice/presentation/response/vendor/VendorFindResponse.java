package com.fastline.vendorservice.presentation.response.vendor;

import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;

import java.util.UUID;

public record VendorFindResponse(
        UUID vendorId,
        String vendorName,
        VendorType vendorType,
        VendorAddress address,
        UUID hubId
) {
}
