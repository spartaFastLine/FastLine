package com.fastline.authservice.presentation.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserHubIdResponseDto {
    private final UUID hubId;
    public UserHubIdResponseDto(UUID hubId) {
        this.hubId = hubId;
    }
}
