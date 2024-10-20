package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

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

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(MEAL_3_ID, USER_ID);
        Assert.assertEquals(userMeal3, actual);
        actual = service.get(MEAL_8_ID, ADMIN_ID);
        Assert.assertEquals(adminMeal1, actual);
    }

    @Test
    public void getNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.get(MEAL_3_ID, ADMIN_ID));
        Assert.assertThrows(NotFoundException.class, () -> service.get(MEAL_9_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_4_ID, USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> service.get(MEAL_4_ID, USER_ID));
        service.delete(MEAL_9_ID, ADMIN_ID);
        Assert.assertThrows(NotFoundException.class, () -> service.get(MEAL_9_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.delete(MEAL_3_ID, ADMIN_ID));
        Assert.assertThrows(NotFoundException.class, () -> service.delete(MEAL_8_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(null, null, USER_ID);
        List<Meal> expected = getSortedUserMeals();
        Assert.assertEquals(expected, actual);
        actual = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        expected = getUserMealsBetweenDates();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Meal> actualMeals = service.getAll(USER_ID);
        List<Meal> expectedMeals = getSortedUserMeals();
        Assert.assertEquals(actualMeals, expectedMeals);
        actualMeals = service.getAll(ADMIN_ID);
        expectedMeals = getSortedAdminMeals();
        Assert.assertEquals(actualMeals, expectedMeals);
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        Assert.assertEquals(userMeal5, service.get(MEAL_5_ID, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), ADMIN_ID);
        Integer createdId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(createdId);
        Assert.assertEquals(newMeal, created);
        Assert.assertEquals(newMeal, service.get(createdId, ADMIN_ID));

        created = service.create(getNew(), USER_ID);
        createdId = created.getId();
        newMeal = getNew();
        newMeal.setId(createdId);
        Assert.assertEquals(newMeal, created);
        Assert.assertEquals(newMeal, service.get(createdId, USER_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        Assert.assertThrows(DuplicateKeyException.class, () -> service.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "some desc", 333), USER_ID));
        userMeal5.setDateTime(userMeal6.getDateTime());
        Assert.assertThrows(DuplicateKeyException.class, () -> service.create(userMeal5, USER_ID));
    }
}