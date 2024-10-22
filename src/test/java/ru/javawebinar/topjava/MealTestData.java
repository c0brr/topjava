package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_1_ID = START_SEQ + 3;
    public static final int MEAL_2_ID = START_SEQ + 4;
    public static final int MEAL_3_ID = START_SEQ + 5;
    public static final int MEAL_4_ID = START_SEQ + 6;
    public static final int MEAL_5_ID = START_SEQ + 7;
    public static final int MEAL_6_ID = START_SEQ + 8;
    public static final int MEAL_7_ID = START_SEQ + 9;
    public static final int MEAL_8_ID = START_SEQ + 10;
    public static final int MEAL_9_ID = START_SEQ + 11;
    public static final int MEAL_NOT_FOUND_ID = 77;

    public static final Meal userMeal1 = new Meal(MEAL_1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2 = new Meal(MEAL_2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3 = new Meal(MEAL_3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal4 = new Meal(MEAL_4_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal userMeal5 = new Meal(MEAL_5_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal6 = new Meal(MEAL_6_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal7 = new Meal(MEAL_7_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal adminMeal1 = new Meal(MEAL_8_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal adminMeal2 = new Meal(MEAL_9_ID, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);
    public static final Meal notFoundMeal = new Meal(MEAL_NOT_FOUND_ID, LocalDateTime.of(2024, Month.JUNE, 5, 17, 0), "Завтрак", 700);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, Month.JULY, 20, 15, 15), "new description", 20);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal5);
        updated.setDateTime(LocalDateTime.of(2024, Month.JUNE, 15, 12, 20));
        updated.setDescription("updated description");
        updated.setCalories(50);
        return updated;
    }

    public static List<Meal> getSortedUserMeals() {
        List<Meal> userMeals = new ArrayList<>();
        userMeals.add(userMeal7);
        userMeals.add(userMeal6);
        userMeals.add(userMeal5);
        userMeals.add(userMeal4);
        userMeals.add(userMeal3);
        userMeals.add(userMeal2);
        userMeals.add(userMeal1);
        return userMeals;
    }

    public static List<Meal> getSortedAdminMeals() {
        List<Meal> adminMeals = new ArrayList<>();
        adminMeals.add(adminMeal2);
        adminMeals.add(adminMeal1);
        return adminMeals;
    }

    public static List<Meal> getUserMealsBetweenDates() {
        List<Meal> userMeals = new ArrayList<>();
        userMeals.add(userMeal3);
        userMeals.add(userMeal2);
        userMeals.add(userMeal1);
        return userMeals;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(List<Meal> actual, List<Meal> expected) {
        Assertions.assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}