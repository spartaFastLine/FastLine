package com.fastline.authservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum UserOrderBy {
	username,
	hubId,
	role,
	status;

	public static void checkValid(String sortBy) {
		for (UserOrderBy orderBy : UserOrderBy.values()) {
			if (orderBy.name().equals(sortBy)) {
				return;
			}
		}
		throw new CustomException(ErrorCode.INVALID_SORYBY);
	}
}
