<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="meal.title"/></title>
    <link rel="stylesheet" href="/topjava/resources/css/style.css" type="text/css">
</head>
<body>
<section>
    <h3><a href="index.jsp"><spring:message code="app.home"/></a></h3>
    <hr/>
    <h2><spring:message code="meal.title"/></h2>
    <form method="get" action="meals">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt><th><spring:message code="meal.fromDateInclusive"/></th></dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><th><spring:message code="meal.toDateInclusive"/></th></dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt><th><spring:message code="meal.fromTimeInclusive"/></dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.toTimeInclusive"/></dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="meal.filter"/></button>
    </form>
    <hr/>
    <a href="meals/mealForm"><spring:message code="meal.addMeal"/></a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meal.date"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-meal-excess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/mealForm/${meal.id}"><spring:message code="meal.update"/></a></td>
                <td><a href="meals/delete/${meal.id}"><spring:message code="meal.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>