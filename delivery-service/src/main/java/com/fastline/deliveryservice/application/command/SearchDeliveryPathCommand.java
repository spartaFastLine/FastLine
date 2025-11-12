package com.fastline.deliveryservice.application.command;

public record SearchDeliveryPathCommand(int page, int size, String sortBy, String direction) {}
