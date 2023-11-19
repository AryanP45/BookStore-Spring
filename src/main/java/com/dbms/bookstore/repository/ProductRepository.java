package com.dbms.bookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dbms.bookstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByCategory_Id(int id);
}