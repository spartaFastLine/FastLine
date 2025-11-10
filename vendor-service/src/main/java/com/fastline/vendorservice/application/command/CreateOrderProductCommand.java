package com.fastline.vendorservice.application.command;

import java.util.UUID;

public record CreateOrderProductCommand(UUID productId, Integer quantity) {}
