package com.fastline.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private boolean success;
	private String message;
	private T data;
	private String errorCode;

	// 성공 응답 생성 (메시지만 있는 경우)
	public static <T> ApiResponse<T> ofSuccess(String message) {
		return new ApiResponse<>(true, message, null, null);
	}

	// 성공 응답 생성 (데이터와 메시지가 있는 경우)
	public static <T> ApiResponse<T> ofSuccess(String message, T data) {
		return new ApiResponse<>(true, message, data, null);
	}

	// 실패 응답 생성
	public static <T> ApiResponse<T> ofFailure(String message, String errorCode) {
		return new ApiResponse<>(false, message, null, errorCode);
	}

	// 실패 응답 생성 (메시지만 있는 경우)
	public static <T> ApiResponse<T> ofFailure(String message) {
		return new ApiResponse<>(false, message, null, null);
	}
}
