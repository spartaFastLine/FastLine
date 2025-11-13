package com.fastline.authservice.presentation.dto.request;

import java.util.UUID;

public record UserManagerUpdateRequest(UUID hubId, String status, String deliveryType) {}
