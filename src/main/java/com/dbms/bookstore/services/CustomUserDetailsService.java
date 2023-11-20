package com.dbms.bookstore.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dbms.bookstore.model.CustomUserDetail;
import com.dbms.bookstore.model.User;
import com.dbms.bookstore.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 Optional<User> userOptional = userRepository.findUserByEmail(username);
		    User user = userOptional.get();
		    return new CustomUserDetail(user);
	}

}
