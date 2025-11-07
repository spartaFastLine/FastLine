package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum VendorType {
	PRODUCER,
	CONSUMER;

	public static VendorType fromString(String value) {
		if (value == null) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		VendorType vendorType;
		try {
			vendorType = VendorType.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.VALIDATION_ERROR);
		}
		return vendorType;
	}
}
