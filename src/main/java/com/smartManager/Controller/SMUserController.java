package com.smartManager.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smartManager.DAO.SMUserRepository;
import com.smartManager.Entity.SMContactEntity;
import com.smartManager.Entity.SMUserEntity;

@Controller
@RequestMapping(value = "/user")
public class SMUserController {

	@Autowired
	SMUserRepository repository;
	public SMUserController() {
		// TODO Auto-generated constructor stub
	}
	
	@ModelAttribute
	public void addCommonDataAttribute(ModelAndView modelAndView, Principal principal) {
		String userName = principal.getName();
		SMUserEntity findByEmail = repository.findByEmail(userName);
		modelAndView.addObject("user", findByEmail);
	}
	
	@GetMapping(value = "/dashboard")
	public ModelAndView openHomePage(ModelAndView modelAndView, Principal principal) {
		modelAndView.addObject("User-DashBoard", "Home - Smart Contact Manager");
		modelAndView.setViewName("Generic/SMUserDashboard");
		return modelAndView;
	}
	
	@GetMapping(value = "/add-contact")
	public ModelAndView openAddContact(ModelAndView modelAndView, Principal principal) {
		modelAndView.addObject("newContact", new SMContactEntity());
		modelAndView.addObject("Add-Contact", "Smart Contact Manager");
		modelAndView.setViewName("Generic/SMAddNewUser");
		return modelAndView;
	}

}
