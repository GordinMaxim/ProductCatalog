<%@page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>:: Product List ::</title>
</head>
<body>

	<div style="margin: 30px">
		<p>
		<form method="post">
			<input type="hidden" name="pageName" value="listProduct" />
			<table border=0>
				<thead>
					<tr>
						<th>Category</th>
						<th>Name</th>
						<th>Price from</th>
						<th>Price to</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" name="product_category" /></td>
						<td><input type="text" name="product_name" /></td>
						<td><input type="text" name="product_price_from" /></td>
						<td><input type="text" name="product_price_to" /></td>
						<td><input type="submit" value="Search" /></td>
					</tr>
				</tbody>
			</table>
		</form>
		</p>
	</div>

	<table border=1>
		<thead>
			<tr>
				<th>ID</th>
				<th>Category</th>
				<th>Name</th>
				<th>Price</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${products}" var="product">
				<tr>
					<td><c:out value="${product.id}" /></td>
					<td><c:out value="${product.category.name}" /></td>
					<td><c:out value="${product.name}" /></td>
					<td><c:out value="${product.price}" /></td>
					<td><a
						href="CatalogServlet?action=edit&prodId=<c:out value="${product.id}"/>">Update</a></td>
					<td><a
						href="CatalogServlet?action=delete&prodId=<c:out value="${product.id}"/>">Delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<p>
		<a href="CatalogServlet?action=insert">Add Product</a>
	</p>
</body>
</html>