<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<h2>${meal.id == null ? "Add Meal" : "Edit Meal"}</h2>
<form action='meals' method="post">
    <p style="background-color: ghostwhite">DateTime:<input type="datetime-local" name="dateTime"
                                                            style="margin-left: 30px"
                                                            value="${meal.dateTime.format(MealsUtil.DATE_TIME_FORMATTER)}"/>
    </p>
    <p style="background-color: ghostwhite">Description:<input type="text" name="description" maxlength="300"
                                                               style="margin-left: 19px" size="40"
                                                               value="${meal.description}"/></p>
    <p style="background-color: ghostwhite">Calories:<input type="number" name="calories" required min="0" max="2000000"
                                                            style="margin-left: 39px"
                                                            value="${meal.calories}"/>
    </p>
    <input type="hidden" name="id" value="${meal.id}"/>
    <p><input type="submit" value="Save"/>
        <button onclick="window.history.back()" type="button" style="margin-left: 10px">Cancel</button>
    </p>
</form>
</body>
</html>