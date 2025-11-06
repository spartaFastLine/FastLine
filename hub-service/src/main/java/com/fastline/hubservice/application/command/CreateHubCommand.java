package com.fastline.hubservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHubCommand {
    private UUID centralHubId;
    private boolean isCentral;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
