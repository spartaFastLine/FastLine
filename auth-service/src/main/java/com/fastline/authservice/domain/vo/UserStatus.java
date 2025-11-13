package com.fastline.authservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum UserStatus {
	PENDING,
	APPROVE,
	REJECTED,
	SUSPENSION,
	DELETED;

	public static UserStatus from(String value) {
		if (value == null) return null;

		for (UserStatus status : values()) {
			if (status.name().equalsIgnoreCase(value)) {
				return status;
			}
		}
		throw new CustomException(ErrorCode.VALIDATION_ERROR);
	}
}
