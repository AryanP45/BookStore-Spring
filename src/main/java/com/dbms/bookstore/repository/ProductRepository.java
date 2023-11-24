package com.dbms.bookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbms.bookstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByCategory_Id(int id);
	
	@Query("SELECT p FROM Product p where p.name LIKE %?1% OR p.description LIKE %?1%")
	public List<Product> getSearchedProducts(String str);
	
}