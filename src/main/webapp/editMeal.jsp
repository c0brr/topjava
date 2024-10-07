<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>
<form action='meals' method="post" accept-charset="utf-8">
    <jsp:useBean id="dateTime" scope="request" type="java.time.LocalDateTime"/>
    <jsp:useBean id="description" scope="request" type="java.lang.String"/>
    <jsp:useBean id="calories" scope="request" type="java.lang.Integer"/>
    <jsp:useBean id="id" scope="request" type="java.lang.Integer"/>
    <p>DateTime:<input type="datetime-local" name="dateTime" value="${dateTime.format(MealsUtil.DATE_TIME_FORMATTER)}"/></p>
    <p>Description:<input type="text" name="description" maxlength="300" value="${description}"/></p>
    <p>Calories:<input type="text" name="calories" minlength="1" maxlength="9" pattern="[0-9]+" value="${calories}"/></p>
    <input type="hidden" name="id" value="${id}"/>
    <p><input type="submit" value="Save"/><button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>