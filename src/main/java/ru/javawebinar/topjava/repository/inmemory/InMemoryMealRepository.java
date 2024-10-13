package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
//        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Еда 2 пользователя Завтрак", 500), 2);
//        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Еда 2 пользователя Обед", 1000), 2);
//        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Еда 2 пользователя Ужин", 500), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        int mealId = meal.getId();
        return repository.get(mealId).getUserId() == userId ?
                repository.computeIfPresent(mealId, (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = (repository.containsKey(id) && repository.get(id).getUserId() == userId) ? repository.remove(id) : null;
        return meal != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return doGetAllFiltered(meal -> meal.getUserId() == userId);
    }

    @Override
    public List<Meal> getFilteredByDates(int userId, LocalDate startDate, LocalDate endDate) {
        return doGetAllFiltered(meal -> meal.getUserId() == userId &&
                DateTimeUtil.isDateBetweenClosed(meal.getDate(), startDate, endDate));
    }

    private List<Meal> doGetAllFiltered(Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

