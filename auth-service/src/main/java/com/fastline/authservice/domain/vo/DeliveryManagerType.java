package com.fastline.authservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum DeliveryManagerType {
	HUB_DELIVERY,
	VENDOR_DELIVERY;

	public static DeliveryManagerType from(String value) {
		if (value == null) return null;

		for (DeliveryManagerType type : values()) {
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new CustomException(ErrorCode.VALIDATION_ERROR);
	}
}
