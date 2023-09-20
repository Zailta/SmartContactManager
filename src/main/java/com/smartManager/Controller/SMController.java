package com.smartManager.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smartManager.Entity.SMEntity;

import jakarta.validation.Valid;

@RestController
public class SMController {
	
	//View Openers:
	@GetMapping(value = "/home")
	public ModelAndView openHomePage(ModelAndView modelAndView) {
		modelAndView.setViewName("SMHome");
		return modelAndView;
	}
	
	//Execution Commands:
	@GetMapping(value = "/SMLogin")
	public ModelAndView openLoginForm(ModelAndView modelAndView) {
		
		//sending an empty Object to Bind data in case of errors.
		modelAndView.addObject("loginData", new SMEntity());
		modelAndView.setViewName("SMLoginForm");
		return modelAndView;
	}
	@PostMapping(value = "/process")
	public ModelAndView loginFormValidation(@Valid @ModelAttribute("loginData") SMEntity smEntity, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		
		
		if(bindingResult.hasErrors()) {
			 modelAndView.setViewName("SMLoginForm");
			 return modelAndView;
		}
		
		return modelAndView;
	}
	
	

}
