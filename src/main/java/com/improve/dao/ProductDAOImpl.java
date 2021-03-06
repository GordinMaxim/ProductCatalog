package com.improve.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.improve.model.Category;
import com.improve.model.Product;
import com.improve.util.PersistenceUtil;

public class ProductDAOImpl implements ProductDAO {

	public ProductDAOImpl() {
		PersistenceUtil.buildEntityManagerFactory();
	}

	public List<Product> getAllProducts() {
		EntityManager em = PersistenceUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		List<Product> products = (List<Product>) em.createQuery("from Product").getResultList();
		transaction.commit();
		return products;
	}

	public Product getProductById(long id) {
		EntityManager em = PersistenceUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Product product = em.find(Product.class, id);
		transaction.commit();
		return product;
	}

	public void deleteProduct(Product product) {
		EntityManager em = PersistenceUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		try {
			if (null != product) {
				product = em.find(Product.class, product.getId());
				em.remove(product);
			}
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	public void updateProduct(Product product) {
		EntityManager em = PersistenceUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		try {
			em.merge(product);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	public void addProduct(Product product) {
		EntityManager em = PersistenceUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		try {
			em.merge(product);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	public void addProduct(Category category, String name, BigDecimal price) {
		Product product = new Product();
		product.setCategory(category);
		product.setName(name);
		product.setPrice(price);
		addProduct(product);
	}
}
