package com.fastline.hubservice.domain.spec;

import java.util.UUID;

/**
 * Spec object for creating a Hub aggregate in a domain-pure way.
 * Keep validation/invariants inside Hub.create(spec).
 */
public record CreateHubSpec(
        UUID centralHubId,
        boolean isCentral,
        String name,
        String address,
        Double latitude,
        Double longitude
) {


}
