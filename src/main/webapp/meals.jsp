<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=edit">Add Meal</a></p>
<table>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color: ${mealTo.excess ? 'red' : 'forestgreen'}">
            <td>${mealTo.dateTime.format(MealsUtil.DATE_TIME_FORMATTER)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
            <td><a href="meals?action=edit&id=${mealTo.id}">Update</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>