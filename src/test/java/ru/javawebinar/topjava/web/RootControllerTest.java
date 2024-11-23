package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.mealsTo;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        String resource = "users";
        doPerform(resource).andExpect(model().attribute(resource,
                new AssertionMatcher<List<User>>() {
                    @Override
                    public void assertion(List<User> actual) throws AssertionError {
                        USER_MATCHER.assertMatch(actual, admin, guest, user);
                    }
                }));
    }

    @Test
    void getMeals() throws Exception {
        String resource = "meals";
        doPerform(resource).andExpect(model().attribute(resource, mealsTo));
    }

    private ResultActions doPerform(String resource) throws Exception {
        return perform(get("/" + resource))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(resource))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + resource + ".jsp"));
    }
}