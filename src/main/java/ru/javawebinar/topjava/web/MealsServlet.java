package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealsMemoryStorage;
import ru.javawebinar.topjava.storage.MealsStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    private static final String ADD_OR_UPDATE = "/adEditMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private MealsStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new MealsMemoryStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            log.debug("forward to meals.jsp");
            req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
        } else {
            switch (action) {
                case "delete":
                    log.debug("redirect to MealsServlet after delete");
                    storage.delete(Integer.parseInt(req.getParameter("id")));
                    resp.sendRedirect(req.getRequestURI());
                    break;
                case "edit":
                    log.debug("forward to adEditMeal.jsp");
                    String mealId = req.getParameter("id");
                    setAttributes(req, mealId == null ? new Meal(LocalDateTime.now(), "", 0) :
                            storage.get(Integer.parseInt(mealId)), mealId);
                    req.getRequestDispatcher(ADD_OR_UPDATE).forward(req, resp);
                    break;
                default:
                    log.debug("redirect to MealsServlet");
                    resp.sendRedirect(req.getRequestURI());
            }
        }
    }

    private void setAttributes(HttpServletRequest req, Meal meal, String mealId) {
        req.setAttribute("meal", meal);
        req.setAttribute("action", mealId == null ? "Add Meal" : "Edit Meal");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        int calories;
        try {
            calories = Integer.parseInt(req.getParameter("calories"));
        } catch (NumberFormatException e) {
            calories = 0;
        }
        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(req.getParameter("dateTime")), req.getParameter("description"), calories);
        if (id.isEmpty()) {
            log.debug("new meal added");
            storage.add(meal);
        } else {
            log.debug("meal with id {} updated", id);
            storage.update(meal);
        }
        log.debug("redirect to MealsServlet");
        resp.sendRedirect(req.getRequestURI());
    }
}