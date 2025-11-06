package com.fastline.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* 필요 시 성공코드 추가 후 사용 */
@Getter
@RequiredArgsConstructor
public enum SuccessCode {

	// 회원 (user)
	USER_SAVE_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
	USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	USER_READ_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
	USER_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정 성공"),
	PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "비밀번호 수정 성공"),
	USER_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "회원 삭제 성공"),

    // 업체 (vendor)
    VENDOR_SAVE_SUCCESS(HttpStatus.CREATED, "업체 등록 성공");
	private final HttpStatus httpStatus;
	private final String message;
}
