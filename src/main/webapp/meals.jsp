<%--
  Created by IntelliJ IDEA.
  User: Lazy
  Date: 15.10.2022
  Time: 8:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meals</title>
    <style>
        .colorGreen {
            color: green;
            text-align-all: center
        }

        .colorRed {
            color: Red;
            text-align-all: center
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h3><a href=meals?action=create>Add meal</a></h3>
<table border="2" cellpadding="10">
    <th>ID</th>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th>Update</th>
    <th>Delete</th>
    <c:forEach var="mealTo" items="${mealToList}">
        <tr class= ${mealTo.isExcess()? 'colorRed': 'colorGreen'}>
            <td> ${mealTo.getId()} </td>
            <td> ${fn:formatDateTime(mealTo.getDateTime())} </td>
            <td> ${mealTo.getDescription()} </td>
            <td> ${mealTo.getCalories()} </td>
            <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.getId()}"/>">Update</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.getId()}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
