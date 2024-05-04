package com.smartManager.DAO;

import org.springframework.data.repository.CrudRepository;

import com.smartManager.Entity.SMUserEntity;

public interface SMUserRepository extends CrudRepository<SMUserEntity, String>{
	public SMUserEntity findByEmail(String email);

}
