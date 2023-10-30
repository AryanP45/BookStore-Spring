package com.dbms.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbms.bookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
