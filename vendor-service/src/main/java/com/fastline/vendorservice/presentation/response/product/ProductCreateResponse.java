package com.fastline.vendorservice.presentation.response.product;

import com.fastline.vendorservice.domain.vo.Money;
import com.fastline.vendorservice.domain.vo.Stock;
import java.util.UUID;

public record ProductCreateResponse(UUID productId, Stock stock, Money price, UUID vendorId) {}
