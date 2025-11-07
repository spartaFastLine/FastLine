package com.fastline.vendorservice.application.command;

import java.util.UUID;

public record CreateProductCommand(String name, Integer stock, Double price, UUID vendorId) {}
