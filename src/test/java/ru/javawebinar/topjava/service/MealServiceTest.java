package ru.javawebinar.topjava.service;

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
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-jdbc-repository.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-service-web.xml"
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
    public void getForUser() {
        MealTestData.assertMatch(service.get(USER_MEAL_5_ID, USER_ID), userMeal5);
    }

    @Test
    public void getForAdmin() {
        MealTestData.assertMatch(service.get(ADMIN_MEAL_1_ID, ADMIN_ID), adminMeal1);
    }

    @Test
    public void getNotFoundUserMealForAdmin() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_5_ID, ADMIN_ID));
    }

    @Test
    public void getNotFoundAdminMealForUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_2_ID, USER_ID));
    }

    @Test
    public void getNotFoundForNotExistingMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void deleteForUser() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteForAdmin() {
        service.delete(ADMIN_MEAL_2_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_2_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFoundUserMealForAdmin() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFoundAdminMealForUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteNotFoundNotExistingMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusiveWithSingleDate() {
        List<Meal> actual = service.getBetweenInclusive(null, LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        List<Meal> expected = Arrays.asList(userMeal3, userMeal2, userMeal1);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusiveWithTwoDates() {
        List<Meal> actual = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.JANUARY, 31), USER_ID);
        List<Meal> expected = Arrays.asList(userMeal7, userMeal6, userMeal5, userMeal4);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getAllForUser() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = Arrays.asList(userMeal7, userMeal6, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getAllForAdmin() {
        List<Meal> actual = service.getAll(ADMIN_ID);
        List<Meal> expected = Arrays.asList(adminMeal2, adminMeal1);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void update() {
        service.update(MealTestData.getUpdated(), USER_ID);
        MealTestData.assertMatch(service.get(USER_MEAL_5_ID, USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateNotFoundUserMealForAdmin() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(), ADMIN_ID));
    }

    @Test
    public void updateNotFoundNotExistingMeal() {
        assertThrows(NotFoundException.class, () -> service.update(notFoundMeal, USER_ID));
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
                () -> service.create(new Meal(null, userMeal5.getDateTime(), "Завтрак", 1400), USER_ID));
    }
}