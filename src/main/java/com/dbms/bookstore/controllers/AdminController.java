package com.dbms.bookstore.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dbms.bookstore.dao.ProductDao;
import com.dbms.bookstore.model.Category;
import com.dbms.bookstore.model.Product;
import com.dbms.bookstore.services.CategoryService;
import com.dbms.bookstore.services.ProductService;


@Controller
public class AdminController {
	public String uploadDir = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\productImages";
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	
	@GetMapping("/admin")
	public String adminHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Admin page "+authentication.getName());
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
	
	// product routes
	@GetMapping("/admin/products")
	public String getProducts(Model model) {
		model.addAttribute("products" ,productService.getAllProduct());
		return "products";
	}
	
	@GetMapping("/admin/products/add")
	public String getProductAdd(Model model) {
		model.addAttribute("productDAO" ,new ProductDao());
		model.addAttribute("categories",categoryService.getAllCategories());
		return "productsAdd";
	}
	@PostMapping("/admin/products/add")
	public String postProductAdd(@ModelAttribute("productDAO") ProductDao productDao, 
			@RequestParam("productImage")MultipartFile file, @RequestParam("imgName") String imgName
			)throws IOException {
		
		Product product = new Product();
		product.setId(productDao.getId());
		product.setName(productDao.getName());
		product.setCategory(categoryService.getCategoryById(productDao.getCategoryId()).get());
		product.setPrice(productDao.getPrice());
		product.setDescription(productDao.getDescription());
		product.setPages(productDao.getPages());
		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath  = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}else {
			imageUUID = imgName;
		}
		System.out.println(uploadDir);
		
		product.setImageName(imageUUID);
		productService.addProduct(product);
		
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/delete/{id}")
	public String getProductDelete(@PathVariable Long id) {
		
		productService.removeProductById(id);
		
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{id}")
	public String getProductUpdate(@PathVariable Long id,Model model) {
		
		Product product=productService.getProductById(id).get();
		ProductDao productDao = new ProductDao();
		productDao.setId(id);
		productDao.setName(product.getName());
		productDao.setDescription(product.getDescription());
		productDao.setPages(product.getPages());
		productDao.setCategoryId(product.getCategory().getId());
		productDao.setPrice(product.getPrice());
		productDao.setImageName(product.getImageName());
		
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("productDAO",productDao);
		
		return "productsAdd";
	}

	
	
}
