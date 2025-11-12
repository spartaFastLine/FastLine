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
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	NOT_HUB_MANAGER(HttpStatus.UNAUTHORIZED, "담당하는 허브가 아닙니다."),
	EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 이름입니다."),
	NOT_PENDING(HttpStatus.BAD_REQUEST, "승인대기 상태가 아닙니다."),
	USER_NOT_APPROVE(HttpStatus.UNAUTHORIZED, "승인된 사용자가 아닙니다."),
	NOT_APPROVE(HttpStatus.BAD_REQUEST, "활성화 상태가 아닙니다."),
	NOT_REJECTED(HttpStatus.BAD_REQUEST, "권한정지 상태가 아닙니다."),
	PASSWORD_NOT_MATCHES(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	PASSWORD_EQUAL(HttpStatus.BAD_REQUEST, "기존의 비밀번호와 동일한 비밀번호입니다."),
	NO_USER_PERMIT_AUTHORITY(HttpStatus.BAD_REQUEST, "사용자 승인 권한이 없습니다."),
	NO_AUTHORITY(HttpStatus.FORBIDDEN, "특정 권한을 가진 사용자만 가능합니다."),
	INVALID_SORYBY(HttpStatus.BAD_REQUEST, "잘못된 정렬 조건입니다"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "인가에 실패하였습니다"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다"),
	UNAUTHORIZED_MEMBER(HttpStatus.FORBIDDEN, "데이터 저장:::인가에 실패하였습니다"),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 조회 실패"), // 나중에 삭제

	// 배송 매니저 (delivery manager)
	DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 매니저 조회 실패"),
	NOT_DELIVERY_MANAGER(HttpStatus.BAD_REQUEST, "배송 매니저만 등록이 가능합니다"),
	EXIST_DELIVERY_MANAGER(HttpStatus.BAD_REQUEST, "이미 배송 매니저로 등록되어 있습니다."),
	DELIVERY_MANAGER_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 배송 매니저입니다."),

	// 업체 (vendor)
	ADDRESS_DUPLICATED(HttpStatus.CONFLICT, "업체 등록 실패"),
	VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 조회 실패"),
	PRODUCT_NAME_DUPLICATED(HttpStatus.CONFLICT, "상품 등록 실패"),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 조회 실패"),
	PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품 재고 부족"),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 조회 실패"),
	ORDER_STATUS_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "주문 상태 변경 실패 - 존재하지 않거나 변경 불가한 주문"),

	// AI
	GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 생성 실패"),

	// 메세지 (message)
	SEND_SLACK_MESSAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메세지 전송 실패");

	private final HttpStatus httpStatus;
	private final String message;
}
