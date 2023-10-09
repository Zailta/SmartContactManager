package com.smartManager.DAO;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.smartManager.Entity.SMContactEntity;

@Repository
public interface SMContactRepository extends CrudRepository<SMContactEntity, Integer>{
	
	public Page<SMContactEntity> findByuser_userID(String userID, Pageable pageable);

}
