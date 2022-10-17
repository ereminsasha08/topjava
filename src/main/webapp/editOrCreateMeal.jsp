<%--
  Created by IntelliJ IDEA.
  User: Lazy
  Date: 15.10.2022
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit and create meal form</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib uri="http://myfunction.com/dataTimeFormat" prefix="f" %>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit and create meal form</h2>
<form method="POST">
    Meal ID :
    <c:choose>
        <c:when test="${meal != null}">
            <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
            <input type="number" readonly="readonly" disabled="disabled" name="mealId"
                   value="<c:out value="${meal.getId()}"/>"/> <br/>
            Data and Time : <input type="datetime-local" name="dataTime"
                                   value="<c:out value="${meal.getDateTime()}" />"/> <br/>
        </c:when>
        <c:otherwise>
            <input type="number" name="mealId" disabled="disabled"> <br/>
            Data and Time : <input type="datetime-local" name="dataTime"
                                   value="2000-01-01T00:00"/> <br/>
        </c:otherwise>
    </c:choose>

    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.getDescription()}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.getCalories()}" />"/> <br/>
    <input type="submit" value="Submit"/>
    <form method="GET" > <input type="submit" value="Cancel" /></form>
</form>
</body>
</html>
