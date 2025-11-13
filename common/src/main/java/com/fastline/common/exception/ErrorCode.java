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
	// 허브/경로 (hub & hub-path)
	START_HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "출발 허브가 없거나 삭제되었습니다."),
	END_HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "도착 허브가 없거나 삭제되었습니다."),
	START_HUB_NOT_FOUND_OR_DELETED(HttpStatus.NOT_FOUND, "출발 허브를 찾을 수 없거나 삭제되었습니다."),
	END_HUB_NOT_FOUND_OR_DELETED(HttpStatus.NOT_FOUND, "도착 허브를 찾을 수 없거나 삭제되었습니다."),
	ROUTE_NOT_FOUND_OR_DELETED(HttpStatus.NOT_FOUND, "경로를 찾을 수 없거나 삭제되었습니다."),
	NO_PATH_AVAILABLE(HttpStatus.NOT_FOUND, "요청한 허브 간 최적 경로를 찾을 수 없습니다."),
	MEMBER_TO_MEMBER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "멤버 허브 간 직접 연결은 허용되지 않습니다(거점 경유 필요)."),
	MEMBER_NOT_CONNECTED_TO_ITS_CENTRAL(HttpStatus.BAD_REQUEST, "멤버 허브는 자신의 거점 허브와만 연결할 수 있습니다."),
	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "경로를 찾을 수 없거나 삭제되었습니다."),
	ROUTE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 경로입니다."),
	ROUTE_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 경로입니다."),
	INVALID_ROUTE_DIRECTION(HttpStatus.BAD_REQUEST, "허용되지 않은 경로 방향입니다."),
	CENTRAL_HUB_DELETE_FORBIDDEN(HttpStatus.BAD_REQUEST, "하위 허브가 있는 거점 허브는 삭제할 수 없습니다."),
	NAVER_API_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "네이버 API 인증에 실패했습니다."),
	NAVER_API_FORBIDDEN(HttpStatus.FORBIDDEN, "네이버 API 접근이 거부되었습니다."),
	NAVER_API_SUBSCRIPTION_REQUIRED(HttpStatus.UNAUTHORIZED, "네이버 API 구독이 필요합니다."),
	NAVER_API_ERROR(HttpStatus.BAD_GATEWAY, "네이버 API 호출 중 오류가 발생했습니다."),
	NO_ACTIVE_HUBS(HttpStatus.NOT_FOUND, "활성 허브가 없습니다."),
	// 배송 매니저 (delivery manager)
	DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 매니저 조회 실패"),
	NOT_DELIVERY_MANAGER(HttpStatus.BAD_REQUEST, "배송 매니저만 등록이 가능합니다"),
	IMPOSSIBLE_ASSIGNMENT(HttpStatus.BAD_REQUEST, "배정 가능한 배송 매니저가 없습니다"),
	EXIST_DELIVERY_MANAGER(HttpStatus.BAD_REQUEST, "이미 배송 매니저로 등록되어 있습니다."),
	DELIVERY_MANAGER_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 배송 매니저입니다."),

	// 업체 (vendor)
	ADDRESS_DUPLICATED(HttpStatus.CONFLICT, "업체 등록 실패"),
	VENDOR_FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 사용자의 요청"),
	VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 조회 실패"),
	VENDOR_HUBID_INVALIDATION(HttpStatus.CONFLICT, "업체의 허브ID가 유효하지 않음"),
	PRODUCT_NAME_DUPLICATED(HttpStatus.CONFLICT, "상품 등록 실패"),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 조회 실패"),
	PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품 재고 부족"),
	PRODUCT_FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 사용자의 요청"),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 조회 실패"),
	ORDER_STATUS_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "주문 상태 변경 실패 - 존재하지 않거나 변경 불가한 주문"),
	ORDER_FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 사용자의 요청"),

	// AI
	GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 생성 실패"),

	// 메세지 (message)
	SEND_SLACK_MESSAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메세지 전송 실패");

	private final HttpStatus httpStatus;
	private final String message;
}
