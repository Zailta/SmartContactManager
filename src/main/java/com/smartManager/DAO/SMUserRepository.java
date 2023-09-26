package com.smartManager.DAO;

import org.springframework.data.repository.CrudRepository;

import com.smartManager.Entity.SMUserEntity;

public interface SMUserRepository extends CrudRepository<SMUserEntity, Integer>{
	public SMUserEntity findByEmail(String email);

}
