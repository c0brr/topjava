package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsMemoryStorage implements MealsStorage {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MealsMemoryStorage() {
        for (Meal meal : MealsUtil.meals) {
            add(meal);
        }
    }

    @Override
    public Meal add(Meal meal) {
        Integer id = counter.getAndIncrement();
        meal.setId(id);
        return meals.put(id, meal);
    }

    @Override
    public Meal update(Meal meal) {
        return meals.replace(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }

    @Override
    public Meal get(Integer id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
