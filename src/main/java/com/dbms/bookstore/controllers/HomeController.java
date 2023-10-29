package com.dbms.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dbms.bookstore.dao.ProductDao;
import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.services.CategoryService;
import com.dbms.bookstore.services.ProductService;

@Controller
public class HomeController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	
	@GetMapping({"/","/home"})
	public String home(Model model) {
		return "index";
	}
	
	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("products",productService.getAllProduct());
		return "shop";
	}
	
	@GetMapping("/shop/category/{id}")
	public String shopByCategory(@PathVariable int id,Model model) {
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("products",productService.getAllProductsByCategoryId(id));
		return "shop";
	}
	
	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(@PathVariable Long id,Model model) {
		
		Product product=productService.getProductById(id).get();
		ProductDao productDao = new ProductDao();
		productDao.setId(id);
		productDao.setName(product.getName());
		productDao.setDescription(product.getDescription());
		productDao.setWeight(product.getWeight());
		productDao.setCategoryId(product.getCategory().getId());
		productDao.setPrice(product.getPrice());
		productDao.setImageName(product.getImageName());
		
		model.addAttribute("product",productService.getProductById(id).get());
		return "viewProduct";
	}
	
}
