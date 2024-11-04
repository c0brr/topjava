package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(MEAL1_ID, USER_ID);
        Meal expected = getExpectedWithUser();
        MEAL_MATCHER.assertMatch(actual, expected);
        USER_MATCHER.assertMatch(actual.getUser(), expected.getUser());
    }

    @Test
    public void assertWrongUser() {
        assertThrows(AssertionError.class, () ->
                USER_MATCHER.assertMatch(service.getWithUser(MEAL1_ID, USER_ID).getUser(), admin));
    }
}
