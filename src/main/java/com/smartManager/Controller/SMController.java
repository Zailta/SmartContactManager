package com.smartManager.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smartManager.Entity.SMUserEntity;

import jakarta.validation.Valid;

@RestController
public class SMController {
	
	/*
	 * View Openers: launch Home Page
	 */
	@GetMapping(value = "/")
	public ModelAndView openHomePage(ModelAndView modelAndView) {
		modelAndView.addObject("title", "Home - Smart Contact Manager");
		modelAndView.setViewName("SMHome");
		return modelAndView;
	}
	
	/*
	 * View Openers: launch SignUp Page
	 */
	@GetMapping(value = "/signup")
	public ModelAndView openSignUpPage(ModelAndView modelAndView) {
		modelAndView.addObject("user", new SMUserEntity());
		modelAndView.addObject("title", "Sign UP - Smart Contact Manager");
		modelAndView.setViewName("SMSignUp");
		return modelAndView;
	}
	
	/*
	 * View Openers: launch About Page
	 */
	@GetMapping(value = "/about")
	public ModelAndView openaboutPage(ModelAndView modelAndView) {
		modelAndView.addObject("title", "About - Smart Contact Manager");
		modelAndView.setViewName("SMAbout");
		return modelAndView;
	}
	/*
	 * View Openers: launch Login Page
	 */
	@GetMapping(value = "/login")
	public ModelAndView openloginPage(ModelAndView modelAndView) {
		modelAndView.addObject("title", "Login - Smart Contact Manager");
		modelAndView.setViewName("SMLogin");
		return modelAndView;
	}
	
	//Execution Commands:
	
	@PostMapping(value = "/register")
	public ModelAndView loginFormValidation(@Valid @ModelAttribute("user") SMUserEntity smUserEntity, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		if(bindingResult.hasErrors()) {
			 modelAndView.setViewName("SMSignUp");
			 return modelAndView;
		}
		
		
		return modelAndView;
	}
	
	

}
