package com.fastline.authservice.domain.model;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;

public enum ManagerOrderBy {
	username,
	hubId,
	type,
	number,
	status;

	public static void checkValid(String sortBy) {
		for (ManagerOrderBy orderBy : ManagerOrderBy.values()) {
			if (orderBy.name().equals(sortBy)) {
				return;
			}
		}
		throw new CustomException(ErrorCode.INVALID_SORYBY);
	}
}
