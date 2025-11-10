package com.fastline.common.security.model;

import lombok.Getter;

@Getter
public enum UserRole {
	MASTER(Authority.MASTER),
	HUB_MANAGER(Authority.HUB_MANAGER),
	DELIVERY_MANAGER(Authority.DELIVERY_MANAGER),
	VENDOR_MANAGER(Authority.VENDOR_MANAGER);

	private final String authority;

	UserRole(String authority) {
		this.authority = authority;
	}

	public static class Authority {
		public static final String MASTER = "ROLE_MASTER";
		public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
		public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";
		public static final String VENDOR_MANAGER = "ROLE_VENDOR_MANAGER";
	}
}
