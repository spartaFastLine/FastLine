package com.fastline.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* 필요 시 에러코드 추가 후 사용 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 공통
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

	// 회원 (user)
	DUPLICATED(HttpStatus.CONFLICT, "회원가입 실패"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "인가 실패"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 실패"),
	LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인 실패"),
	LOGOUT_FAIL(HttpStatus.UNAUTHORIZED, "로그아웃 실패"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 조회 실패"),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 변경 실패"),
	ALREADY_DELETED_USER(HttpStatus.CONFLICT, "회원 삭제 실패");

	private final HttpStatus httpStatus;
	private final String message;
}
