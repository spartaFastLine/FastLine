package com.fastline.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastline.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException)
			throws IOException, ServletException {
		log.error("접근 권한 오류 발생::: error : {}", authException.getMessage(), authException);
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
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
