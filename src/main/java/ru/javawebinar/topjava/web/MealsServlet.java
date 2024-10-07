package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MemoryMapStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsServlet extends HttpServlet {
    private static final Storage STORAGE = new MemoryMapStorage();
    private static final Logger log = getLogger(MealsServlet.class);
    private static final String ADD_OR_UPDATE = "/editMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";

    //data for test:
    static {
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        STORAGE.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            req.setAttribute("mealsTo", MealsUtil.getTo(STORAGE.getAll(), MealsUtil.CALORIES_PER_DAY));
            log.debug("forward to meals.jsp");
            req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
        } else {
            if (action.equals("delete")) {
                STORAGE.delete(Integer.parseInt(req.getParameter("id")));
                log.debug("redirect to MealsServlet after delete");
                resp.sendRedirect(req.getRequestURI());
            } else if (action.equals("edit")) {
                String mealId = req.getParameter("id");
                if (mealId == null) {
                    req.setAttribute("dateTime", LocalDateTime.now());
                    req.setAttribute("description", "");
                    req.setAttribute("calories", 0);
                    req.setAttribute("id", -1);
                } else {
                    Meal meal = STORAGE.get(Integer.parseInt(mealId));
                    req.setAttribute("dateTime", meal.getDateTime());
                    req.setAttribute("description", meal.getDescription());
                    req.setAttribute("calories", meal.getCalories());
                    req.setAttribute("id", meal.getId());
                }
                log.debug("forward to editMeal.jsp");
                req.getRequestDispatcher(ADD_OR_UPDATE).forward(req, resp);
            } else {
                resp.sendRedirect(req.getRequestURI());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        int calories;
        try {
            calories = Integer.parseInt(req.getParameter("calories"));
        } catch (NumberFormatException e) {
            calories = 0;
        }
        if (id < 1) {
            STORAGE.add(new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), calories));
            log.debug("new meal added");
        } else {
            STORAGE.update(new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), calories, id));
            log.debug("meal with id " + id + " updated");
        }
        req.setAttribute("mealsTo", MealsUtil.getTo(STORAGE.getAll(), MealsUtil.CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
    }
}