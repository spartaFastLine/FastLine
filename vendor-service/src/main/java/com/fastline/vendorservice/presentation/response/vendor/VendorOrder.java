package com.fastline.vendorservice.presentation.response.vendor;

import com.fastline.vendorservice.domain.vo.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record VendorOrder(UUID orderId, OrderStatus orderStatus, LocalDateTime arrivalTime) {}
