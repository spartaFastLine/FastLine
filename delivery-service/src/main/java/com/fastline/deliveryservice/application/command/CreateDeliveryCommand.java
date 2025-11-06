package com.fastline.deliveryservice.application.command;

import java.util.List;
import java.util.UUID;

public record CreateDeliveryCommand(
        UUID orderId,
        UUID vendorSenderId,
        UUID vendorReceiverId,
        UUID startHubId,
        UUID endHubId,
        String address,
        String recipientUsername,
        String recipientSlackId,
        Long vendorDeliveryManagerId,
        List<CreateDeliveryPathCommand> paths
) {}
