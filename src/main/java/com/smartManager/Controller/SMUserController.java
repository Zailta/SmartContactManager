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

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

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
	@Autowired
	JavaMailSender javaMailSender;

	public SMUserController() {
		// TODO Auto-generated constructor stub
	}

	@ModelAttribute
	public void addCommonDataAttribute(ModelAndView modelAndView, Principal principal) {
		String userName = principal.getName();
		SMUserEntity findByEmail = repository.findByEmail(userName);
		modelAndView.addObject("user", findByEmail);
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
	
	@GetMapping(value = "/delete/{contactID}")
	public RedirectView deleteUser(@PathVariable("contactID") Integer contactID, Principal principal, HttpSession session) {
		Optional<SMContactEntity> findById = contactRepository.findById(contactID);
		if(principal.getName().equals(findById.get().getUser().getEmail())) {
			SMContactEntity smContactEntity = findById.get();
			smContactEntity.setUser(null);
			contactRepository.delete(findById.get());
			session.setAttribute("message", new SMMessageHandler("Contact Deleted Succesfully! ", "alert-success"));
		}else {
			session.setAttribute("message", new SMMessageHandler("You don't have delete priveleges for this contact! ", "alert-danger"));
		}
	
		return new RedirectView(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/view-contacts/0");
	}
	
	//open the update page:
	@GetMapping(value = "/update/{contactID}")
	public ModelAndView updateUser(ModelAndView modelAndView, @PathVariable("contactID") Integer contactID, Principal principal, HttpSession session) {
		Optional<SMContactEntity> findById = contactRepository.findById(contactID);
		if(principal.getName().equals(findById.get().getEmail())) {
			modelAndView.addObject("contact",findById.get());
			modelAndView.addObject("profile", Base64.getEncoder().encodeToString(findById.get().getProfilePicture()));
			modelAndView.setViewName("Generic/SMUpdateSingleUser");
		}
		else {
			session.setAttribute("message", new SMMessageHandler("You don't have update priveleges for this contact! ", "alert-danger"));
		}
		
		return modelAndView;
	}
	
	@PostMapping(value = "/update-user")
	public RedirectView processUpdateForm(@ModelAttribute SMContactEntity contactEntity, @RequestParam("profileImage")MultipartFile file, HttpSession session) {
		Optional<SMContactEntity> findById = contactRepository.findById(contactEntity.getContactID());
		if(file.isEmpty()) {
			byte[] profilePicture = findById.get().getProfilePicture();
			contactEntity.setProfilePicture(profilePicture);
		}
		else {
			try {
				contactEntity.setProfilePicture(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		contactEntity.setUser(findById.get().getUser());
		this.contactRepository.save(contactEntity);
		session.setAttribute("message", new SMMessageHandler("Contact Updated Succesfully! ", "alert-success"));
		return new RedirectView(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/user/update/"+contactEntity.getContactID());
	}
	
	@GetMapping(value = "/email/{contactID}")
	public ModelAndView sendEmail(ModelAndView modelAndView, @PathVariable("contactID") Integer contactID, Principal principal, HttpSession session) {
		Optional<SMContactEntity> findById = contactRepository.findById(contactID);
		if(principal.getName().equals(findById.get().getEmail())) {
			modelAndView.addObject("contact",findById.get());
			modelAndView.addObject("useremail",principal.getName());
			modelAndView.setViewName("Generic/SMEmailForm");
		}
		else {
			session.setAttribute("message", new SMMessageHandler("You don't have Email priveleges for this contact! ", "alert-danger"));
		}
		return modelAndView;
	}
	@PostMapping(value = "/invoke-email")
	public ModelAndView processEmail(ModelAndView modelAndView, Principal principal, HttpSession session) {
		Integer contactID = Integer.valueOf(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("contactID"));
		Optional<SMContactEntity> findById = contactRepository.findById(contactID);
		if(principal.getName().equals(findById.get().getEmail())) {
			String to = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getParameter("to");
			String from = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getParameter("from");
			//code to invoke Email Service:
			StringBuilder body = new StringBuilder();
			body.append("<table class=\"table\">\r\n"
					+ "        <thead>\r\n"
					+ "          <tr>\r\n"
					+ "			  <th>Name</th>\r\n"
					+ "              <th>Email</th>\r\n"
					+ "              <th>About</th>\r\n"
					+ "              <th>Phone Number</th>\r\n"
					+ "              <th>Nick Name</th>\r\n"
					+ "          </tr>\r\n"
					+ "        </thead>\r\n"
					+ "\r\n"
					+ "        <tbody>\r\n"
					+ "       ");
			body.append("<tbody>\r\n"
					+ "			<tr>\r\n"
					+ "				<td>\r\n"
					+ "					<a>\r\n"
					+ "						<div>\r\n"
					+ "							<span "+findById.get().getName()+"\"></span>\r\n"
					+ "						</div>\r\n"
					+ "					</a>\r\n"
					+ "				</td>\r\n"
					+ "				<td "+findById.get().getEmail()+"\"></td>\r\n"
					+ "				<td "+findById.get().getAbout()+"\"></td>\r\n"
					+ "				<td "+findById.get().getPhone()+"\"></td>\r\n"
					+ "				<td "+findById.get().getNickName()+"\"></td>\r\n"
					+ "			</tr>\r\n"
					+ "		</tbody>\r\n"
					+ "		</table>");
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setSubject("Smart Contact Manager: Contact-Details");
			mailMessage.setTo(to); 
			mailMessage.setFrom(from);
			mailMessage.setText(body.toString()); 
			javaMailSender.send(mailMessage);
			
			modelAndView.addObject("contact",findById.get());
			modelAndView.addObject("useremail",principal.getName());
			modelAndView.setViewName("Generic/SMEmailForm");
		}
		else {
			session.setAttribute("message", new SMMessageHandler("You don't have Email priveleges for this contact! ", "alert-danger"));
		}
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
