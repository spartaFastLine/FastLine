package com.fastline.hubservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO used for updating existing hub path information.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathUpdateRequest {

    @NotNull
    private UUID startHubId;

    @NotNull
    private UUID endHubId;

    @NotNull
    private Double distance;

    private Boolean active;
}
