package com.fastline.vendorservice.infrastructure.external.dto.delivery;

public record DeliveryRequestDto(
        String orderId, String vendorSenderId, String vendorReceiverId, String recipientUsername, String recipientSlackId,
        String address
) {
}
