package com.dbms.bookstore.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.dbms.bookstore.global.GlobalData;	
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
		model.addAttribute("cartCount",GlobalData.cart.size());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Home page "+authentication.getName());
		return "index";
	}
	
	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("cartCount",GlobalData.cart.size());
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("products",productService.getAllProduct());
		return "shop";
	}
	
	@GetMapping("/shop/category/{id}")
	public String shopByCategory(@PathVariable int id,Model model) {
		model.addAttribute("cartCount",GlobalData.cart.size());
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("products",productService.getAllProductsByCategoryId(id));
		return "shop";
	}
	
	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(@PathVariable Long id,Model model) {
		model.addAttribute("cartCount",GlobalData.cart.size());		
		model.addAttribute("product",productService.getProductById(id).get());
		return "viewProduct";
	}
	
	
}
