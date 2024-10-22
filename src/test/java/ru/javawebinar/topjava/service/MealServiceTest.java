package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_5_ID, USER_ID);
        MealTestData.assertMatch(meal, userMeal5);
        meal = service.get(MEAL_8_ID, ADMIN_ID);
        MealTestData.assertMatch(meal, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_5_ID, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_8_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void delete() {
        assertNotNull(service.get(MEAL_1_ID, USER_ID));
        service.delete(MEAL_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_1_ID, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_9_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(null, LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        List<Meal> expected = getUserMealsBetweenDates();
        Assertions.assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = getSortedUserMeals();
        MealTestData.assertMatch(actual, expected);
        actual = service.getAll(ADMIN_ID);
        expected = getSortedAdminMeals();
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MEAL_5_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(userMeal3, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.update(adminMeal2, USER_ID));
        assertThrows(NotFoundException.class, () -> service.update(notFoundMeal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer createdMealId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(createdMealId);
        assertMatch(created, newMeal);
        assertMatch(service.get(createdMealId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class,
                () -> service.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1400), USER_ID));
    }
}