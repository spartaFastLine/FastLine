package com.fastline.hubservice.domain.vo;

import java.util.UUID;

/** 허브 목록 조회를 위한 도메인 VO */
public record HubSearchFilterVo(String name, String address, UUID centralHubId, Boolean isCentral) {
	public static HubSearchFilterVo of(
			String name, String address, UUID centralHubId, Boolean isCentral) {
		return new HubSearchFilterVo(name, address, centralHubId, isCentral);
	}
}
