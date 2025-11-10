package com.fastline.vendorservice.presentation.response.product;

import com.fastline.vendorservice.domain.vo.Money;
import com.fastline.vendorservice.domain.vo.Stock;

public record ProductUpdateResponse(String name, Stock stock, Money price) {}
