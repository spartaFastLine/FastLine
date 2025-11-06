package com.fastline.common.exception;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAuthorizationDenied(AuthorizationDeniedException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("권한 부족::: uri: {}, errormessage:{}",uri, e.getMessage(), e);

        return ResponseUtil.failureResponse(
                ErrorCode.NO_AUTHORITY.getMessage()+" ::: "+uri+"의 권한을 확인하세요", ErrorCode.NO_AUTHORITY.name(), ErrorCode.NO_AUTHORITY.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("서버 내부 오류 발생: {}", e.getMessage(), e);

		return ResponseUtil.failureResponse(
				e.getMessage(), ErrorCode.SERVER_ERROR.name(), ErrorCode.SERVER_ERROR.getHttpStatus());
	}
}
