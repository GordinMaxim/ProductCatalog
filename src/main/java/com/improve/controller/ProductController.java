package com.improve.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.improve.dao.ProductDAO;
import com.improve.dao.ProductDAOImpl;
import com.improve.model.Category;
import com.improve.model.Product;
import com.improve.util.PersistenceUtil;

public class ProductController extends HttpServlet {

	private static String ERROR = "/error.jsp";
	private static String INSERT_OR_EDIT = "/product.jsp";
	private static String LIST_PRODUCT = "/listProduct.jsp";
	private static int PRICE_SCALE = 2;

	String forward;
	ProductDAO productDAO;

	public ProductController() {
		super();
		productDAO = new ProductDAOImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String action = request.getParameter("action");
		System.out.println("GET ACTION: " + action);
		PersistenceUtil.buildEntityManagerFactory();
		if (action.equals("list")) {
			forward = LIST_PRODUCT;
			List<Product> products = productDAO.getAllProducts();
			request.setAttribute("products", products);
		} else if (action.equals("edit")) {
			forward = INSERT_OR_EDIT;
			String id = request.getParameter("prodId");
			Product product = productDAO.getProductById(Long.parseLong(id));
			EntityManager em = PersistenceUtil.getEntityManager();
			List<Category> categories = (List<Category>) em.createQuery("FROM Category").getResultList();
			request.setAttribute("product", product);
			request.setAttribute("categories", categories);
			request.setAttribute("productCat", product.getCategory().getId());
		} else if (action.equals("delete")) {
			forward = LIST_PRODUCT;
			String id = request.getParameter("prodId");
			Product product = new Product();
			product.setId(Long.parseLong(id));
			productDAO.deleteProduct(product);
			List<Product> products = productDAO.getAllProducts();
			request.setAttribute("products", products);
		} else {
			forward = INSERT_OR_EDIT;
			EntityManager em = PersistenceUtil.getEntityManager();
			List<Category> categories = (List<Category>) em.createQuery("FROM Category").getResultList();
			request.setAttribute("categories", categories);
			request.setAttribute("productCat", categories.get(0).getId());

		}
		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String action = request.getParameter("pageName");

		System.out.println("POST ACTION: " + action);
		if (action.equals("listProduct")) {
			String category = request.getParameter("product_category");
			String name = request.getParameter("product_name");
			String priceFromStr = request.getParameter("product_price_from");
			String priceToStr = request.getParameter("product_price_to");
			String queryParams = category + name + priceFromStr + priceToStr;
			if (queryParams.equals("")) {
				List<Product> products = productDAO.getAllProducts();
				request.setAttribute("products", products);
				RequestDispatcher view = request.getRequestDispatcher(LIST_PRODUCT);
				view.forward(request, response);
				return;
			}

			BigDecimal priceFrom = BigDecimal.valueOf(Double.MIN_VALUE);
			BigDecimal priceTo = BigDecimal.valueOf(Double.MAX_VALUE);
			if (!priceFromStr.equals("")) {
				try {
					priceFrom = new BigDecimal(priceFromStr);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					RequestDispatcher view = request.getRequestDispatcher(ERROR);
					view.forward(request, response);
					return;
				}
			}
			if (!priceToStr.equals("")) {
				try {
					priceTo = new BigDecimal(priceToStr);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					RequestDispatcher view = request.getRequestDispatcher(ERROR);
					view.forward(request, response);
					return;
				}
			}
			priceFrom.setScale(2, RoundingMode.HALF_UP);
			priceTo.setScale(2, RoundingMode.HALF_UP);

			PersistenceUtil.buildEntityManagerFactory();
			EntityManager em = PersistenceUtil.getEntityManager();
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			List<Category> categories = (List<Category>) em
					.createQuery("SELECT c FROM Category c " + "WHERE c.name LIKE :catName")
					.setParameter("catName", category + "%").getResultList();
			Query productQuery = em.createQuery("SELECT p FROM Product p WHERE " + "(p.category IN :catNames) AND "
					+ "(p.name LIKE :prodName) AND " + "(p.price BETWEEN :priceFrom AND :priceTo)");
			productQuery.setParameter("catNames", categories);
			productQuery.setParameter("prodName", name + "%");
			productQuery.setParameter("priceFrom", priceFrom);
			productQuery.setParameter("priceTo", priceTo);
			List<Product> products = (List<Product>) productQuery.getResultList();
			request.setAttribute("products", products);
			transaction.commit();
		} else if (action.equals("addOrEditProduct")) {
			Product product = new Product();
			String id = request.getParameter("id");
			PersistenceUtil.buildEntityManagerFactory();
			EntityManager em = PersistenceUtil.getEntityManager();
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			Category category = em.find(Category.class, Long.parseLong(request.getParameter("category_name")));
			transaction.commit();
			product.setCategory(category);
			product.setName(request.getParameter("product_name"));
			BigDecimal productPrice = null;
			String price = request.getParameter("product_price");
			if (!price.equals("")) {
				try {
					productPrice = new BigDecimal(price);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					RequestDispatcher view = request.getRequestDispatcher(ERROR);
					view.forward(request, response);
					return;
				}
			}
			product.setPrice(productPrice);
			
			if (id == null || id.isEmpty()) {

				productDAO.addProduct(product);

			} else {

				product.setId(Long.parseLong(id));
				productDAO.updateProduct(product);

			}
			
			request.setAttribute("products", productDAO.getAllProducts());
			System.out.println("AddOrEditProduct finished");
		}
		RequestDispatcher view = request.getRequestDispatcher(LIST_PRODUCT);
		view.forward(request, response);
	}
}
