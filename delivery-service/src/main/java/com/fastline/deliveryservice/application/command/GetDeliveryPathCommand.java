package com.fastline.deliveryservice.application.command;

import java.util.UUID;

public record GetDeliveryPathCommand(UUID deliveryId, UUID pathId) {}
