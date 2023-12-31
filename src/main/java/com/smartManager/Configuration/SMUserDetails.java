package com.smartManager.Configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartManager.Entity.SMEntity;
import com.smartManager.Entity.SMUserEntity;

public class SMUserDetails implements UserDetails {

	SMUserEntity userEntity;

	public SMUserDetails(SMUserEntity userEntity) {
		super();
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userEntity.getRole());
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {

		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {

		return userEntity.getEmail();
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
