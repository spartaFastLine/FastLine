package com.fastline.common.response;

import com.fastline.common.exception.ErrorCode;
import com.fastline.common.success.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

	// 성공 응답 생성 (데이터와 메시지, 상태코드가 있는 경우)
	public static <T> ResponseEntity<ApiResponse<T>> successResponse(
            SuccessCode successCode, T data) {
		ApiResponse<T> response = ApiResponse.ofSuccess(successCode.getMessage(), data);
		return new ResponseEntity<>(response, successCode.getHttpStatus());
	}

	// 성공 응답 생성 (메시지와 상태코드만 있는 경우)
	public static <T> ResponseEntity<ApiResponse<T>> successResponse(SuccessCode successCode) {
		ApiResponse<T> response = ApiResponse.ofSuccess(successCode.getMessage());
		return new ResponseEntity<>(response, successCode.getHttpStatus());
	}

	// 실패 응답 생성
	public static <T> ResponseEntity<ApiResponse<T>> failureResponse(
			String message, String errorCode, HttpStatus status) {
		ApiResponse<T> response = ApiResponse.ofFailure(message, errorCode);
		return new ResponseEntity<>(response, status);
	}

	// 실패 응답 생성 (메시지와 상태코드만 있는 경우)
	public static <T> ResponseEntity<ApiResponse<T>> failureResponse(ErrorCode errorCode) {
		ApiResponse<T> response = ApiResponse.ofFailure(errorCode.getMessage());
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}
}
