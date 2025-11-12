package com.fastline.authservice.presentation.request;

import lombok.Getter;

@Getter
public class DeliveryManagerMessageDto {
    private final String slackId;
    private final String username;
    private final String email;

    public DeliveryManagerMessageDto(String slackId, String username, String email) {
        this.slackId = slackId;
        this.username = username;
        this.email = email;
    }
}
