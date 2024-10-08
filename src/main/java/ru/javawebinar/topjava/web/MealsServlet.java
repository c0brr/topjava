package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealsStorage;
import ru.javawebinar.topjava.storage.MemoryMealsStorage;
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
    private static final String ADD_OR_UPDATE = "/addOrEditMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private MealsStorage storage;

    @Override
    public void init() {
        storage = new MemoryMealsStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            log.debug("try to set mealsTo attribute");
            req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
            log.debug("try to forward to meals.jsp");
            req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
        } else {
            switch (action) {
                case "delete":
                    log.debug("try to delete");
                    storage.delete(Integer.parseInt(req.getParameter("id")));
                    log.debug("try to redirect to MealsServlet");
                    resp.sendRedirect(req.getRequestURI());
                    break;
                case "edit":
                    log.debug("try to edit");
                    String mealId = req.getParameter("id");
                    req.setAttribute("meal", mealId == null ? new Meal(LocalDateTime.now(), "", 0) :
                            storage.get(Integer.parseInt(mealId)));
                    log.debug("try to forward to addOrEditMeal.jsp");
                    req.getRequestDispatcher(ADD_OR_UPDATE).forward(req, resp);
                    break;
                default:
                    log.debug("try to redirect to MealsServlet");
                    resp.sendRedirect(req.getRequestURI());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        log.debug("try to construct Meal");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(req.getParameter("dateTime")), req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
        if (id.isEmpty()) {
            log.debug("try to add");
            storage.add(meal);
        } else {
            log.debug("try to update meal with id {}", id);
            storage.update(meal);
        }
        log.debug("try to redirect to MealsServlet");
        resp.sendRedirect(req.getRequestURI());
    }
}