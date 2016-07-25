package com.improve.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

		PersistenceUtil.buildEntityManagerFactory();
		if (action.equals("list")) {
			forward = LIST_PRODUCT;
			EntityManager em = PersistenceUtil.getEntityManager();
			List<Product> products = (List<Product>) em.createQuery("from Product").getResultList();
			request.setAttribute("products", products);
		} else if (action.equals("edit")) {
			
		} else if (action.equals("delete")) { 
			
		} else {
			forward = LIST_PRODUCT;
		}
		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EntityManager em = PersistenceUtil.getEntityManager();
		String category = request.getParameter("product_category");
		String name = request.getParameter("product_name");
		String priceFromStr = request.getParameter("product_price_from");
		String priceToStr = request.getParameter("product_price_to");
		String queryParams = category + name + priceFromStr + priceToStr;
		if (queryParams.equals("")) {
			List<Product> products = (List<Product>) em.createQuery("from Product").getResultList();
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
		List<Category> categories = (List<Category>) em
				.createQuery("SELECT c FROM Category c " + "WHERE c.name LIKE :catName")
				.setParameter("catName", category + "%").getResultList();
		Query productQuery = em.createQuery("SELECT p FROM Product p WHERE "
				 + "(p.category IN :catNames) AND "
				+ "(p.name LIKE :prodName) AND " + "(p.price BETWEEN :priceFrom AND :priceTo)");
		 productQuery.setParameter("catNames", categories);
		productQuery.setParameter("prodName", name + "%");
		productQuery.setParameter("priceFrom", priceFrom);
		productQuery.setParameter("priceTo", priceTo);
		List<Product> products = (List<Product>) productQuery.getResultList();
		request.setAttribute("products", products);

		RequestDispatcher view = request.getRequestDispatcher(LIST_PRODUCT);
		view.forward(request, response);
	}
}
