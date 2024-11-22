package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        doPerform("users",
                new AssertionMatcher<List<User>>() {
                    @Override
                    public void assertion(List<User> actual) throws AssertionError {
                        USER_MATCHER.assertMatch(actual, admin, guest, user);
                    }
                });
    }

    @Test
    void getMeals() throws Exception {
        doPerform("meals",
                new AssertionMatcher<List<MealTo>>() {
                    @Override
                    public void assertion(List<MealTo> actual) throws AssertionError {
                        MEALTO_MATCHER.assertMatch(actual, mealTo7, mealTo6, mealTo5, mealTo4, mealTo3, mealTo2, mealTo1);
                    }
                });
    }

    private <T> void doPerform(String resource, AssertionMatcher<T> assertionMatcher) throws Exception {
        perform(get("/" + resource))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(resource))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + resource + ".jsp"))
                .andExpect(model().attribute(resource, assertionMatcher));
    }
}