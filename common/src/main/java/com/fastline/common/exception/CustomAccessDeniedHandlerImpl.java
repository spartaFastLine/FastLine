package com.fastline.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastline.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class CustomAccessDeniedHandlerImpl implements AccessDeniedHandler {
	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException {
		String uri = request.getRequestURI();
		log.error(
				"접근 권한 오류 발생::: url : {}, error : {}",
				uri,
				accessDeniedException.getMessage(),
				accessDeniedException);
		ErrorCode errorCode = ErrorCode.FORBIDDEN;
		// 메시지에 URI 추가

		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");
		response
				.getWriter()
				.write(
						new ObjectMapper()
								.writeValueAsString(
										ApiResponse.ofFailure(
												errorCode.getMessage(), errorCode.getHttpStatus().toString())));
	}
}
