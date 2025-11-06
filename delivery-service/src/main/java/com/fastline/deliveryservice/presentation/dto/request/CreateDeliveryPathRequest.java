package com.fastline.deliveryservice.presentation.dto.request;

import java.util.UUID;

public record CreateDeliveryPathRequest(
        int sequence,
        UUID fromHubId,
        UUID toHubId,
        int expDistance,
        int expDuration,
        Long deliveryManagerId
) {}