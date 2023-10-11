package com.smartManager.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.smartManager.DAO.SMContactRepository;
import com.smartManager.DAO.SMUserRepository;
import com.smartManager.Entity.SMContactEntity;
import com.smartManager.Entity.SMUserEntity;
import com.smartManager.Helper.SMMessageHandler;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class SMUserController {

	@Autowired
	SMUserRepository repository;
	@Autowired
	SMContactRepository contactRepository;

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
	public ModelAndView openHomePage(ModelAndView modelAndView) {
		modelAndView.addObject("title", "Home - Smart Contact Manager");
		modelAndView.setViewName("Generic/SMUserDashboard");
		return modelAndView;
	}

	@GetMapping(value = "/add-contact")
	public ModelAndView openAddContact(ModelAndView modelAndView) {
		modelAndView.addObject("contact", new SMContactEntity());
		modelAndView.addObject("title", "Add-Contacts Smart Contact Manager");
		modelAndView.setViewName("Generic/SMAddNewUser");
		return modelAndView;
	}
	
	@GetMapping(value = "/view-contacts/{pageNumber}")
	public ModelAndView openViewContact(ModelAndView modelAndView, Principal principal, @PathVariable("pageNumber") Integer pageNumber) {
		modelAndView.addObject("title", "Contacts - Smart Contact Manager");
		PageRequest contactsPerPage = PageRequest.of(pageNumber, 5);
		SMUserEntity user = repository.findByEmail(principal.getName());
		Page<SMContactEntity> findByuser = contactRepository.findByuser_userID(user.getUserID(), contactsPerPage);
		byte[] profilePicture = findByuser.getContent().get(0).getProfilePicture();
		modelAndView.addObject("profile", Base64.getEncoder().encodeToString(profilePicture));
		modelAndView.addObject("contactsList", findByuser);
		modelAndView.addObject("pageNumber", pageNumber);
		modelAndView.addObject("totalPages", findByuser.getTotalPages());
		modelAndView.setViewName("Generic/SMViewContacts");
		return modelAndView;
	}
	
	@GetMapping(value = "/view-contact/{contactID}")
	public ModelAndView openIndividualView(ModelAndView modelAndView, @PathVariable("contactID") Integer contactID, Principal principal) {
		Optional<SMContactEntity> findById = contactRepository.findById(contactID);
		if(principal.getName().equals(findById.get().getUser().getEmail())) {
		modelAndView.addObject("contact", findById.get());
		modelAndView.addObject("profile", Base64.getEncoder().encodeToString(findById.get().getProfilePicture()));
		}
		modelAndView.setViewName("Generic/SMViewSingleContact");
		return modelAndView;
	}
	

	@PostMapping(value = "/process-contact")
	public ModelAndView processContact(ModelAndView modelAndView, @ModelAttribute SMContactEntity contact,
			Principal principal, @RequestParam("profileImage")MultipartFile file, HttpSession session) throws IOException {
		try {
			
			SMUserEntity user = this.repository.findByEmail(principal.getName());
			contact.setProfilePicture(file.getBytes());
			contact.setUser(user);
			contact.setEnabledStatus(true);
			user.getContacts().add(contact);
			this.repository.save(user);
			modelAndView.addObject("contact", new SMContactEntity());
			modelAndView.setViewName("Generic/SMAddNewUser");
			session.setAttribute("message", new SMMessageHandler("Contact Added Succesfully! ", "alert-success"));
			return modelAndView;
			
		} catch (Exception e) {
			modelAndView.addObject("contact", new SMContactEntity());
			session.setAttribute("message", new SMMessageHandler("Something went wrong: "+e.getMessage().toString(), "alert-danger"));
			modelAndView.setViewName("Generic/SMAddNewUser");
			return modelAndView;
			
		}
		
	}

}
