package com.fastline.vendorservice.presentation.response.vendor;

import java.util.List;
import java.util.UUID;

public record VendorProductResponse(UUID vendorId, List<VendorProduct> products) {}
