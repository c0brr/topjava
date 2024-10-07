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
    private final Storage storage = new MemoryMapStorage();
    private static final Logger log = getLogger(MealsServlet.class);
    private static final String ADD_OR_UPDATE = "/editMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";

    {
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String forward;
//        String action = req.getParameter("action");
//
//        if (action == null) {
//            forward = LIST_MEALS;
//            req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
//        } else {
//            if (action.equals("delete")) {
//                forward = LIST_MEALS;
//                storage.delete(Integer.parseInt(req.getParameter("id")));
//                req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
//            } else {
//                forward = ADD_OR_UPDATE;
//                String mealId = req.getParameter("id");
//                if (mealId == null) {
//                    req.setAttribute("dateTime", LocalDateTime.now());
//                    req.setAttribute("description", "");
//                    req.setAttribute("calories", 0);
//                    req.setAttribute("id", -1);
//                } else {
//                    Meal meal = storage.get(Integer.parseInt(mealId));
//                    req.setAttribute("dateTime", meal.getDateTime());
//                    req.setAttribute("description", meal.getDescription());
//                    req.setAttribute("calories", meal.getCalories());
//                    req.setAttribute("id", meal.getId());
//                }
//            }
//        }
//        req.getRequestDispatcher(forward).forward(req, resp);


//        String forward;
//        String action = req.getParameter("action");
//
//        if (action == null) {
//            forward = LIST_MEALS;
//            req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
//            req.getRequestDispatcher(forward).forward(req, resp);
//        } else {
//            if (action.equals("delete")) {
//                //forward = LIST_MEALS;
//                storage.delete(Integer.parseInt(req.getParameter("id")));
//                req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
//                resp.sendRedirect(req.getRequestURI());
//            } else {
//                forward = ADD_OR_UPDATE;
//                String mealId = req.getParameter("id");
//                if (mealId == null) {
//                    req.setAttribute("dateTime", LocalDateTime.now());
//                    req.setAttribute("description", "");
//                    req.setAttribute("calories", 0);
//                    req.setAttribute("id", -1);
//                } else {
//                    Meal meal = storage.get(Integer.parseInt(mealId));
//                    req.setAttribute("dateTime", meal.getDateTime());
//                    req.setAttribute("description", meal.getDescription());
//                    req.setAttribute("calories", meal.getCalories());
//                    req.setAttribute("id", meal.getId());
//                }
//                req.getRequestDispatcher(forward).forward(req, resp);
//            }
//        }
        String forward;
        String action = req.getParameter("action");

        if (action == null) {
            forward = LIST_MEALS;
            req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher(forward).forward(req, resp);
        } else {
            if (action.equals("delete")) {
                storage.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getRequestURI());
            } else {
                forward = ADD_OR_UPDATE;
                String mealId = req.getParameter("id");
                if (mealId == null) {
                    req.setAttribute("dateTime", LocalDateTime.now());
                    req.setAttribute("description", "");
                    req.setAttribute("calories", 0);
                    req.setAttribute("id", -1);
                } else {
                    Meal meal = storage.get(Integer.parseInt(mealId));
                    req.setAttribute("dateTime", meal.getDateTime());
                    req.setAttribute("description", meal.getDescription());
                    req.setAttribute("calories", meal.getCalories());
                    req.setAttribute("id", meal.getId());
                }
                req.getRequestDispatcher(forward).forward(req, resp);
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
            storage.add(new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), calories));
        } else {
            storage.update(new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), calories, id));
        }
        req.setAttribute("mealsTo", MealsUtil.getTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
    }
}