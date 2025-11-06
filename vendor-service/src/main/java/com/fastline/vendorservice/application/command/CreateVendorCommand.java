package com.fastline.vendorservice.application.command;

import java.util.UUID;

public record CreateVendorCommand(
        String name,
        String type,
        String city,
        String district,
        String roadName,
        String zipCode,
        UUID hubId
) {
}
