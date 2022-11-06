<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="/topjava/resources/css/style.css" type="text/css">
</head>
<body>
<section>
    <h3><a href="index.html"><spring:message code="app.home"/></a></h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h2>
        <c:choose>
            <c:when test="${meal.id == null}">
                <spring:message code="mealForm.create"/>
            </c:when>
            <c:otherwise>
                <spring:message code="mealForm.update"/>
            </c:otherwise>
        </c:choose>
    </h2>
    <form method="post" action="mealForm">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="mealForm.dateTime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="mealForm.save"/></button>
        <button onclick="history.back()"><spring:message code="mealForm.goBack"/></button>
    </form>
</section>
</body>
</html>
