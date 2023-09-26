package com.smartManager.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartManager.DAO.SMUserRepository;
import com.smartManager.Entity.SMUserEntity;

public class SMUserDetailsService implements UserDetailsService
{	@Autowired
	SMUserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SMUserEntity findByEmail = repository.findByEmail(username);
		if (findByEmail ==  null) {
			
			throw new UsernameNotFoundException("The User does not Exist in the DB");
		}
		SMUserDetails smUserDetails = new SMUserDetails(findByEmail);
		return smUserDetails;
	}

}
