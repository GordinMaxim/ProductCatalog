<%@page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>:: Add Product ::</title>
</head>
<body>
	<form method="POST" action='CatalogServlet'>
		<input type="hidden" name="pageName" value="addOrEditProduct" />


		<table border=0>
			<tbody>
				<tr>
					<input type="hidden" name="id" value="${product.id}" />
				</tr>
				<tr>
					<td>Category</td>
					<td><select name="category_name">
							<c:forEach items="${categories}" var="category">
								<option value="${category.id}"
									${category.id == productCat ? 'selected="selected"' : ''}>
									${category.name}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td>Name</td>
					<td><input type="text" value="${product.name}"
						name="product_name" /></td>
				</tr>
				<tr>
					<td>Price</td>
					<td><input type="text" value="${product.price}"
						name="product_price" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Save" /></td>
				</tr>
			</tbody>
		</table>

	</form>
</body>
</html>