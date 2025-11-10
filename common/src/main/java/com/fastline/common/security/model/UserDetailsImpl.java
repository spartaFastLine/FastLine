package com.fastline.common.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//일종의 UserDto
public class UserDetailsImpl implements UserDetails {
	@Getter
    private final Long userId;
	private final String username;
	private final String password;
	@Getter
    private final UserRole role;

	public UserDetailsImpl(Long userId, String username, String password, String role) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.role = UserRole.valueOf(role);

	}

    @Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

    // UserDetails 필수 구현체로 권한체크인데 필요한지 모르겠음 - Security에서 사용
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new GrantedAuthorityImpl(role.getAuthority()));
	}

}
