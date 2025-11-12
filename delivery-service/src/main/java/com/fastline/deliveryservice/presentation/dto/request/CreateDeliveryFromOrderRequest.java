package com.fastline.deliveryservice.presentation.dto.request;

import java.util.UUID;

public record CreateDeliveryFromOrderRequest(
        UUID orderId,
        UUID vendorSenderId,
        UUID vendorReceiverId,
        String recipientUsername,
        String recipientSlackId,
        String address
) {}

