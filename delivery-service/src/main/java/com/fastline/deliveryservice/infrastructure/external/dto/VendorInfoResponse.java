package com.fastline.deliveryservice.infrastructure.external.dto;

import java.util.UUID;

public record VendorInfoResponse(
        UUID startHubId,
        UUID endHubId
) {}
