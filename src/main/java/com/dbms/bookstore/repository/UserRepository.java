package com.dbms.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbms.bookstore.model.User;


public interface UserRepository extends JpaRepository<User, Integer>{
	 Optional<User> findUserByEmail(String email);
}
