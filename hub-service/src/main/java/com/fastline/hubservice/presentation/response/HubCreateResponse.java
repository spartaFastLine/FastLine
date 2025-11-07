package com.fastline.hubservice.presentation.response;

import java.util.UUID;

/** 허브 생성 응답 DTO */
public record HubCreateResponse(UUID hubId, String message) {}
