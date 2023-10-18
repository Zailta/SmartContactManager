package com.smartManager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartManager.DAO.SMContactRepository;
import com.smartManager.DAO.SMUserRepository;
import com.smartManager.Entity.SMContactEntity;
import com.smartManager.Entity.SMUserEntity;

@RestController
public class SMSearchController {
	@Autowired
	private SMUserRepository repository;
	@Autowired
	private SMContactRepository contactRepository;

	@GetMapping(value = "search/{query}")
	public ResponseEntity<?> searchHandlerMethod(@PathVariable("query")String query, Principal principal){
		SMUserEntity user = this.repository.findByEmail(principal.getName());
		List<SMContactEntity> findByNameContainingAndUser = this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(findByNameContainingAndUser);
	}
}
