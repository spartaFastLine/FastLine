package com.fastline.authservice.application.result;

import com.fastline.authservice.domain.model.User;

import java.util.UUID;


public record UserResult(Long userId,
                         String email,
                         String username,
                         String role,
                         String slackId,
                         String status,
                         UUID hubId,
                         String deliveryType,
                         Long deliveryNumber) {
    public UserResult(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                user.getSlackId(),
                user.getStatus().name(),
                user.getHubId(),
                user.getDeliveryManager() != null ? user.getDeliveryManager().getType().name() : null,
                user.getDeliveryManager() != null ? user.getDeliveryManager().getNumber() : null
        );
    }
}
