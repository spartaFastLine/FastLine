package com.fastline.vendorservice.presentation.response.vendor;

import java.util.List;
import java.util.UUID;

public record VendorOrderResponse(UUID vendorId, List<VendorOrder> vendorOrders) {}
