package com.fastline.vendorservice.presentation.response.vendor;

import com.fastline.vendorservice.domain.vo.Money;
import com.fastline.vendorservice.domain.vo.Stock;
import java.util.UUID;

public record VendorProduct(UUID productId, Stock stock, Money price) {}
