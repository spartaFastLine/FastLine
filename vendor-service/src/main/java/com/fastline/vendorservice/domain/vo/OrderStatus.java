package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum OrderStatus {
	READY,
	DELIVERING,
	COMPLETED,
	CANCELLED;

	public static OrderStatus fromString(String value) {
		if (value == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		OrderStatus orderStatus;
		try {
			orderStatus = OrderStatus.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.VALIDATION_ERROR);
		}
		return orderStatus;
	}
}
