package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealsStorage implements MealsStorage {
    private final AtomicInteger counter = new AtomicInteger(1);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MemoryMealsStorage() {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public Meal add(Meal meal) {
        int id = counter.getAndIncrement();
        meal.setId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        Meal replacedMeal = meals.replace(meal.getId(), meal);
        return replacedMeal == null ? null : meal;
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
