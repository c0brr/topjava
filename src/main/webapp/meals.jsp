<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>
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
    <h3>${SecurityUtil.authUserId() == 1 ? '<a href="users">Users</a>' : ''}</h3>
    <h2>Meals</h2>
<%--    <h4>From Date(include) To Date(include) From Time(include) To Time(exclude)</h4>--%>
    <form method="post" action="meals">
        <input type="hidden" name="action" value="allFiltered">
<%--        From Date(include)--%>
<%--        <input type="date" name="startDate" value="${param.startDate}"/>--%>
<%--        To Date(include)--%>
<%--        <input type="date" name="endDate" value="${param.endDate}"/>--%>
<%--        From Time(include)--%>
<%--        <input type="time" name="startTime" value="${param.startTime}"/>--%>
<%--        To Time(exclude)--%>
<%--        <input type="time" name="endTime" value="${param.endTime}"/>--%>

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
    </div>




        <button type="submit">Filter</button>
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