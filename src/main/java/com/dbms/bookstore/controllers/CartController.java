package com.dbms.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dbms.bookstore.global.GlobalData;
import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.model.User;
import com.dbms.bookstore.repository.UserRepository;
import com.dbms.bookstore.services.ProductService;

@Controller
public class CartController {
	@Autowired
	ProductService productService;
	@Autowired
	UserRepository userRepository;
		
	@GetMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("cartCount",GlobalData.cart.size());
		model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		model.addAttribute("cart",GlobalData.cart);
		return "cart";
	}
	
	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		GlobalData.cart.add(productService.getProductById(id).get());
		User user = userRepository.findUserByEmail(authentication.getName()).get();
		System.out.println("GOT USER" +  user.getFirstName());
		user.setCartProducts(GlobalData.cart);
		userRepository.save(user);
		System.out.println(user.getCartProducts());
		System.out.println("product added to cart");
		return "redirect:/shop";
	}
	
	
	@GetMapping("/cart/removeItem/{index}")
	public String cartItemRemove(@PathVariable int index) {
		GlobalData.cart.remove(index);
		return "redirect:/cart";
	}
	
	@GetMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		return "checkout";
	}
	
	
}
