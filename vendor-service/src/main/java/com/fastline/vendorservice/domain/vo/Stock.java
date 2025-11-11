package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class Stock {

	private Integer stock;

	public Stock adjust(Stock stock) {
		Integer newValue = this.stock + stock.getStock();
		return of(newValue);
	}

	public boolean isLessThan(Integer quantity) {
		return stock < quantity;
	}

	public static Stock of(Integer value) {
		if (value == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Stock stock = new Stock();
		stock.stock = value;
		return stock;
	}
}
