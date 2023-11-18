package com.dbms.bookstore.controllers;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.dbms.bookstore.global.GlobalData;
import com.dbms.bookstore.model.Role;
import com.dbms.bookstore.model.User;
import com.dbms.bookstore.repository.RoleRepository;
import com.dbms.bookstore.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/login")
	public String login() {
		GlobalData.cart.clear();	
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@PostMapping("/register")
	public String registerpost(@ModelAttribute("user")User user,HttpServletRequest httpServletRequest)throws ServletException {
		String pass = user.getPassword();
		user.setPassword(bCryptPasswordEncoder.encode(pass));
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findById(2).get());
		user.setRoles(roles);
		userRepository.save(user);
		httpServletRequest.login(user.getEmail(), user.getPassword());
			
		return "redirect:/";
	}

}
