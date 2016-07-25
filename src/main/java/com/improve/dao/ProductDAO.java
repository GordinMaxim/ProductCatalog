package com.improve.dao;

import java.math.BigDecimal;
import java.util.List;

import com.improve.model.Category;
import com.improve.model.Product;

public interface ProductDAO {

	List<Product> getAllProducts();
	
	Product getProductById(long id);
	
	void addProduct(Category category, String name, BigDecimal price);
}
