<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <c:if test="${SecurityUtil.authUserId() == 1}">
        <h3><a href="users">Users</a></h3>
    </c:if>
    <h2>Meals</h2>
    <form method="post" action="meals">
        <input type="hidden" name="action" value="allFiltered">
        <div class="dInput">
            <div class="input-wrapper">
                <label for="1">From Date(include)</label>
                <input type="date" name="startDate" value="${param.startDate}" id="1"/>
            </div>
            <div class="input-wrapper">
                <label for="2">To Date(include)</label>
                <input type="date" name="endDate" value="${param.endDate}" id="2"/>
            </div>
            <div class="input-wrapper">
                <label for="3">From Time(include)</label>
                <input type="time" name="startTime" value="${param.startTime}" id="3"/>
            </div>
            <div class="input-wrapper">
                <label for="4">To Time(exclude)</label>
                <input type="time" name="endTime" value="${param.endTime}" id="4"/>
            </div>
            <div class="dInput">
                <button type="submit" style="margin-left: 5px; margin-top: 12px;border-radius: 5px">Filter</button>
            </div>
        </div>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>