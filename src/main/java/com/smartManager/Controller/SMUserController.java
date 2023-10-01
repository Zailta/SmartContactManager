package com.smartManager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class SMUserController {

	public SMUserController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping(value = "/dashboard")
	public ModelAndView openHomePage(ModelAndView modelAndView) {
		modelAndView.addObject("User-DashBoard", "Home - Smart Contact Manager");
		modelAndView.setViewName("Generic/SMUserDashboard");
		return modelAndView;
	}

}
