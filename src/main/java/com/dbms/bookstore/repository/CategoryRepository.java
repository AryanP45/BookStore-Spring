package com.dbms.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbms.bookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
