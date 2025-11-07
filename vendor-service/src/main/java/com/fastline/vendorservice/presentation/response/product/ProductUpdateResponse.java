package com.fastline.vendorservice.presentation.response.product;

import com.fastline.vendorservice.domain.vo.Money;

public record ProductUpdateResponse(String name, Integer stock, Money price) {}
