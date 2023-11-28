package com.dbms.bookstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbms.bookstore.global.GlobalData;
import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.model.User;
import com.dbms.bookstore.repository.UserRepository;
import com.dbms.bookstore.services.CategoryService;
import com.dbms.bookstore.services.ProductService;

@Controller
public class HomeController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserRepository userRepository;

	@GetMapping(path = { "/", "/home" })
	public String home(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// authenticated user
		if (authentication.isAuthenticated()) {
			System.out.println(authentication.getPrincipal());
			try {
				User user = userRepository.findUserByEmail(authentication.getName()).get();
				if (GlobalData.cart.isEmpty())
					GlobalData.cart.addAll(user.getCartProducts());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("NO user found with given email id");
			}
		}
		System.out.println(authentication.getName());

		model.addAttribute("cartCount", GlobalData.cart.size());
		return "index";
	}

	@GetMapping("/shop")
	public String shop(Model model,@RequestParam(required = false) String search) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Home page " + authentication.getName());
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("pageno",1);
		System.out.println("cartcountt : " + GlobalData.cart.size());
		List<Product> products;
		if(search == null) {
//			products = productService.getAllProduct();
			products = productService.getProductsByPage(1,10);			
			System.out.println("Showing All products!!");
		}else {
			products = productService.getSearchedProducts(search);
			System.out.println("Results searched for "+search);
		}
		if(products.isEmpty())
			System.out.println("NO RESULTS FUND");
		else {
			model.addAttribute("products",products);
		}
		
		return "shop";
	}
	
	//showing products by size
	@GetMapping("/shop/{pageno}")
	public String shopPages(@PathVariable int pageno,Model model,@RequestParam(required = false) String search) {
		if(pageno < 1) return "redirect:/shop";
		if(pageno > 6) return "redirect:/shop/6";
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("pageno",pageno);
		List<Product> products;
		if(search == null) {
			// fetch products by pageNumber
			products = productService.getProductsByPage(pageno,10);			
			System.out.println("Showing All products!!");
		}else {
			products = productService.getSearchedProducts(search);
			System.out.println("Results searched for "+search);
		}
		if(products.isEmpty())
			System.out.println("NO RESULTS FUND");
		else {
			model.addAttribute("products",products);
		}
		
		return "shop";
	}

	@GetMapping("/shop/category/{id}")
	public String shopByCategory(@PathVariable int id, Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("pageno",1);
		model.addAttribute("products", productService.getAllProductsByCategoryId(id));
		return "shop";
	}

	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(@PathVariable Long id, Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("product", productService.getProductById(id).get());
		return "viewProduct";
	}


}
