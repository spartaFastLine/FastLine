package com.fastline.authservice.application.result;

import java.util.UUID;


public record UserResult(Long userId,
                         String email,
                         String username,
                         String role,
                         String slackId,
                         String status,
                         UUID hubId) {
}
