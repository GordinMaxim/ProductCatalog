package com.improve.dao;

import java.math.BigDecimal;
import java.util.List;

import com.improve.model.Category;
import com.improve.model.Product;

public interface ProductDAO {

	List<Product> getAllProducts();
	
	Product getProductById(long id);
	
	void deleteProduct(Product product);
	
	void updateProduct(Product product);
	
	void addProduct(Product product);
	
	void addProduct(Category category, String name, BigDecimal price);
}
