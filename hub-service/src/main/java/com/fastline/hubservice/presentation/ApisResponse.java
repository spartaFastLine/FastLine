package com.fastline.hubservice.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 공통 API 응답 래퍼 클래스
 *
 * <p>모든 API 응답을 일관된 형식으로 제공 - 성공 응답: data 필드에 실제 데이터 포함 - 실패 응답: ErrorResponse 사용
 * (GlobalExceptionHandler에서 처리)
 *
 * @param <T> 응답 데이터 타입
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApisResponse<T>(boolean success, String message, T data, Long timestamp) {
	/** 성공 응답 생성 (데이터 포함) */
	public static <T> ApisResponse<T> success(T data) {
		return new ApisResponse<>(true, null, data, System.currentTimeMillis());
	}

	/** 성공 응답 생성 (데이터 + 메시지) */
	public static <T> ApisResponse<T> success(T data, String message) {
		return new ApisResponse<>(true, message, data, System.currentTimeMillis());
	}

	/** 성공 응답 생성 (메시지만) */
	public static <T> ApisResponse<T> success(String message) {
		return new ApisResponse<>(true, message, null, System.currentTimeMillis());
	}

	/** 실패 응답 생성 (에러 데이터) */
	public static <T> ApisResponse<T> error(T errorData) {
		return new ApisResponse<>(false, null, errorData, System.currentTimeMillis());
	}
}
