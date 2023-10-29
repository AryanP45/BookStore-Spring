package com.dbms.bookstore.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dbms.bookstore.model.Category;
import com.dbms.bookstore.services.CategoryService;


@Controller
public class AdminController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}
	
	@GetMapping("/admin/categories")
	public String categories(Model model) {
		model.addAttribute("categories",categoryService.getAllCategories());
		return "categories";
	}
	
	@GetMapping("/admin/categories/add")
	public String getcategoriesAdd(Model model) {
		model.addAttribute("category",new Category());
		return "categoriesAdd";
	}
	
	@PostMapping("/admin/categories/add")
	public String postcategoriesAdd(@ModelAttribute ("category")Category category) {
		categoryService.addCategory(category);
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id) {
		categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/update/{id}")
	public String updateCategory(@PathVariable int id,Model model) {
		Optional<Category> category = categoryService.getCategoryById(id);
		if(category.isPresent()) {
			model.addAttribute("category",category.get());
			return "categoriesAdd";
		}else {
			return "404";
		}
		
	}
	
	
}
