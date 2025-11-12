package com.fastline.hubservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents a request to create a hub path between two hubs.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathCreateRequest {

    @NotNull
    private UUID startHubId;

    @NotNull
    private UUID endHubId;

    @NotNull
    private Long distance;

    private Boolean active;
}
