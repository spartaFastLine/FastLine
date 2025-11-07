package com.fastline.vendorservice.application.command;

public record UpdateProductCommand(String name, Integer stock, Double price) {}
