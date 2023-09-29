package com.dailyplanner.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dailyplanner.dto.ContactDto;
import com.dailyplanner.dto.UserDto;
import com.dailyplanner.entity.*;
import com.dailyplanner.repository.ContactRepository;
import com.dailyplanner.service.UserService;

import jakarta.validation.Valid;

@Controller
@CrossOrigin("*")
public class LoginRegisterationController {
	
	Logger log = LoggerFactory.getLogger(AuthController.class);

	private UserService userService;
	private final ContactRepository contactRepository;
	
	public LoginRegisterationController(UserService userService,ContactRepository contactRepository) {
		this.userService = userService;
		this.contactRepository=contactRepository;

	}
	
	
	@GetMapping("/index")
	public String home() {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		log.info("Entering into AuthController :: login");
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		log.info("Exiting into AuthController :: login");
		return "login";
	}
	
	@GetMapping("/contact")
	public String contact(Model model) {
		
		ContactDto contact = new ContactDto();
		model.addAttribute("contact", contact);
		System.out.println("contact here");
		return "contact";
	}
	
	@PostMapping("/contact/save")
	public String contactSave(@Valid @ModelAttribute("contact") ContactDto contact, BindingResult result, Model model) {

		try {

			log.info("Entering into AuthController :: registration");
			System.out.println("contact dto :"+contact);
			
			log.info("Entering into AuthController :: hasErrors");
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "/contact";
			}else {
				
				Contact c = new Contact();
				c.setName(contact.getName());
				c.setEmail(contact.getEmail());
				c.setCreatedDate(new Date());
				c.setNumber(contact.getNumber());
				c.setMessage(contact.getMessage());
				contactRepository.save(c);
				return "redirect:/contact?success";
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	
	// handler method to handle user registration form request
		@GetMapping("/register")
		public String showRegistrationForm(Model model) {

			log.info("Entering into AuthController :: showRegistrationForm");

			UserDto user = new UserDto();
			model.addAttribute("user", user);

			log.info("Exiting into AuthController :: showRegistrationForm");
			return "register";
		}
		
		// handler method to handle user registration form submit request
		@PostMapping("/register/save")
		public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {

			try {

				log.info("Entering into AuthController :: registration");
				User existingEmail = userService.findUserByEmail(userDto.getEmail());

				if (existingEmail != null && existingEmail.getEmail() != null && !existingEmail.getEmail().isEmpty()) {
					result.rejectValue("email", null, "There is already an account registered with the same email");
				}

				if (userDto.getConfirmPassword() != null && userDto.getPassword() != null) {
					if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
						result.rejectValue("password", null, "Password and Confirm Password should be same");
					}
				}

				log.info("Entering into AuthController :: hasErrors");
				if (result.hasErrors()) {
					model.addAttribute("user", userDto);
					return "/register";
				}

				userService.saveUser(userDto);

				log.info("Exiting into AuthController :: registration");
				return "redirect:/register?success";
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}
	
	

}
