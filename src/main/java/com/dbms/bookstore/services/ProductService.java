package com.dbms.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
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
}
