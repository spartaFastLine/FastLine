package com.fastline.deliveryservice.application.command;

public record SearchDeliveryCommand(int page, int size, String sortBy, String direction) {}
