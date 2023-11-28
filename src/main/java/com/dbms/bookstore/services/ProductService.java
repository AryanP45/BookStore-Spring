package com.dbms.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.repository.ProductRepository;

import jakarta.persistence.EntityManager;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	EntityManager entityManager;
	
	public List<Product> getAllProduct(){
		return productRepository.findAll();
	}
	public void addProduct(Product product) {
		productRepository.save(product);
	}
	public void removeProductById(Long id) {
		productRepository.deleteById(id);
	}
	public Optional<Product> getProductById(Long id){
		return productRepository.findById(id);
	}
	public List<Product> getAllProductsByCategoryId(int id){
		return productRepository.findAllByCategory_Id(id);
	}
	public List<Product> getSearchedProducts(String searchString) {
		// code for finding products based on search string from user
		
		return productRepository.getSearchedProducts(searchString);
	}
	public List<Product> getProductsByPage(int pageno,int pageSize){
		int offset = (pageno - 1) * pageSize;

	    return entityManager.createQuery("SELECT p FROM Product p", Product.class)
	            .setFirstResult(offset)
	            .setMaxResults(pageSize)
	            .getResultList();
	}

}
