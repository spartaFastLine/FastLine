package com.fastline.vendorservice.infrastructure.external.dto.message;

public record MessageResponseDto(String success, String message, String data, String errorCode) {}
