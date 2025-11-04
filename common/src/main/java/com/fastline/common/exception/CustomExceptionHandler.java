package com.fastline.common.exception;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "CustomExceptionHandler")
public class CustomExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseUtil.failureResponse(
				errorCode.getMessage(), errorCode.name(), errorCode.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse<Void>> handleValidationException(
			MethodArgumentNotValidException e) {

		List<String> errorMessages =
				e.getBindingResult().getFieldErrors().stream()
						.map(
								fieldError ->
										String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
						.collect(Collectors.toList());

		String combinedMessage = String.join(", ", errorMessages);

		return ResponseUtil.failureResponse(
				combinedMessage,
				ErrorCode.VALIDATION_ERROR.name(),
				ErrorCode.VALIDATION_ERROR.getHttpStatus());
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
		log.error("서버 내부 오류 발생: {}", e.getMessage(), e);

		return ResponseUtil.failureResponse(
				e.getMessage(), ErrorCode.SERVER_ERROR.name(), ErrorCode.SERVER_ERROR.getHttpStatus());
	}
}
