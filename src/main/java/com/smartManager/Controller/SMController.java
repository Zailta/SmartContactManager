package com.smartManager.Controller;

import org.aspectj.bridge.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smartManager.Configuration.SMUserDetailsService;
import com.smartManager.DAO.SMUserRepository;
import com.smartManager.Entity.SMUserEntity;
import com.smartManager.Helper.JWTHelper;
import com.smartManager.Helper.SMMessageHandler;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
public class SMController {
	@Autowired
	private SMUserRepository smUserRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTHelper jwtHelper;
	@Autowired
	private SMUserDetailsService userDetailsService;
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
	@GetMapping(value = "/signin")
	public ModelAndView openloginPage(ModelAndView modelAndView) {
		modelAndView.addObject("title", "Login - Smart Contact Manager");
		modelAndView.setViewName("SMLogin");
		return modelAndView;
	}
	
	//Execution Commands:
	

	@PostMapping(value="/token")
	public void generateAccessToken() {
		 String userName = "azameap@gmail.com";
		 String password = passwordEncoder.encode("Redemption#@1");
		 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		 UserDetails loadUserByUsername = userDetailsService.loadUserByUsername(userName);
		 String token = jwtHelper.generateToken(loadUserByUsername);
		 System.out.println("----------------------------------------------------------------"+token);
	}
	
	@PostMapping(value = "/register")
	public ModelAndView loginFormValidation(@Valid @ModelAttribute("user") SMUserEntity smUserEntity,BindingResult bindingResult, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			
			  if(session.getAttribute("message")!=null) 
				  session.removeAttribute("message");
			 
			if(smUserEntity.getName().equalsIgnoreCase("abc"))
				throw new Exception("not valid");
			if (bindingResult.hasErrors()) {
				modelAndView.setViewName("SMSignUp");
				return modelAndView;
			}
			smUserEntity.setRole("ROLE_USER");
			smUserEntity.setEnabledStatus(true);
			smUserEntity.setPassword(passwordEncoder.encode(smUserEntity.getPassword()));
			SMUserEntity savedResultSet = smUserRepository.save(smUserEntity);
			System.out.println(savedResultSet);
			modelAndView.addObject("user", new SMUserEntity());
			modelAndView.setViewName("SMSignUp");
			session.setAttribute("message", new SMMessageHandler("User Added Succesfully! ", "alert-success"));
			return modelAndView;
		} catch (Exception e) {
			modelAndView.addObject("user", new SMUserEntity());
			session.setAttribute("message", new SMMessageHandler("Something went wrong: "+e.getMessage().toString(), "alert-danger"));
			modelAndView.setViewName("SMSignUp");
			return modelAndView;
		}
		

	}
	

}
