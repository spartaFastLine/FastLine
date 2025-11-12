package com.fastline.vendorservice.infrastructure.external.dto.delivery;

import java.util.UUID;

public record VendorHubIdResponse(
        String startHubId, String endHubId
) {
}
