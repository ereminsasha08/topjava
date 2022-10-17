<%--
  Created by IntelliJ IDEA.
  User: Lazy
  Date: 15.10.2022
  Time: 8:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://myfunction.com/dataTimeFormat" prefix="f" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/mealToIsExcess.css" type="text/css" media="screen"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h3><a href=meals?action=create>Add meal</a></h3>
<table border="2" cellpadding="10">
    <c:forEach var="mealTo" items="${mealToList}">
        <c:choose>
            <c:when test="${mealTo.isExcess()}">
                <tr class = colorRed>
            </c:when>
            <c:otherwise>
                <tr class = colorGreen>
            </c:otherwise>
        </c:choose>
        <td>
            <c:out value="${mealTo.getId()}"/>
        </td>
        <td>
            <c:out value=" ${f:formatLocalDateTime(mealTo.getDateTime(), 'dd.MM.yyyy HH.mm.ss')}"/>
        </td>
        <td>
            <c:out value="${mealTo.getDescription()}"/>
        </td>
        <td>
            <c:out value="${mealTo.getCalories()}"/>
        </td>
        <td>
            <a href="meals?action=edit&mealId=<c:out value="${mealTo.getId()}"/>">Update</a>
        </td>
        <td>
            <a href="meals?action=delete&mealId=<c:out value="${mealTo.getId()}"/>">Delete</a>
        </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
