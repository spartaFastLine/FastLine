package com.fastline.authservice.domain.security;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private final User user;

	public User getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// UserDetails 필수 구현체로 권한체크인데 필요한지 모르겠음 - Security에서 사용
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		UserRole role = user.getRole();
		return List.of(new GrantedAuthorityImpl(role.getAuthority()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
