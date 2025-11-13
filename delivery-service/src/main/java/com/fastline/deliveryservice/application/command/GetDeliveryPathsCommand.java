package com.fastline.deliveryservice.application.command;

import java.util.UUID;

public record GetDeliveryPathsCommand(UUID deliveryId) {}
