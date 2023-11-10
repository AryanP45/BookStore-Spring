package com.dbms.bookstore.global;

import java.util.ArrayList;
import java.util.List;

import com.dbms.bookstore.model.Product;

public class GlobalData {
	public static List<Product> cart;
	static {
		cart = new ArrayList<>();
	}
}	
