package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMapStorage implements Storage {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public void add(Meal meal) {
        meal.setId(COUNTER.getAndIncrement());
        meals.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);
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
