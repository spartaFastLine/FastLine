package com.fastline.vendorservice.presentation.response.product;

import com.fastline.vendorservice.domain.vo.Money;
import java.util.UUID;

public record ProductCreateResponse(UUID productId, Integer stock, Money price, UUID vendorId) {}
