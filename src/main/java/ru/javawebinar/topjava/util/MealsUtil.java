package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealsUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

    public static List<MealTo> getFilteredTo(List<Meal> meals,
                                             LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return doGetTo(meals, meals.stream().filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(),
                startTime, endTime)), caloriesPerDay);
    }

    public static List<MealTo> getTo(List<Meal> meals, int caloriesPerDay) {
        return doGetTo(meals, meals.stream(), caloriesPerDay);
    }

    private static List<MealTo> doGetTo(List<Meal> meals, Stream<Meal> mealStream, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return mealStream
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
