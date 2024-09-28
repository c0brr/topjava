package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        mealsTo = filteredBySingleCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredBySingleStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> datesAndCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate ld = meal.getDateTime().toLocalDate();
            datesAndCalories.put(ld, datesAndCalories.getOrDefault(ld, 0) + meal.getCalories());
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime ldt = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(ldt, meal.getDescription(), meal.getCalories(),
                        datesAndCalories.get(ldt.toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> datesAndCalories = meals.stream()
                .collect(Collectors.toMap(meal ->
                        meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        datesAndCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredBySingleCycle(List<UserMeal> meals, LocalTime startTime,
                                                                 LocalTime endTime, int caloriesPerDay) {
        CopyOnWriteArrayList<UserMealWithExcess> result = new CopyOnWriteArrayList<>();
        AtomicInteger finishedThreads = new AtomicInteger();
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(meals.size());
        Map<LocalDate, Integer> datesAndCalories = new HashMap<>();
        Map<LocalDate, Boolean> datesAndExcesses = new HashMap<>();

        for (UserMeal meal : meals) {
            LocalDate ld = meal.getDateTime().toLocalDate();
            datesAndCalories.put(ld, datesAndCalories.getOrDefault(ld, 0) + meal.getCalories());
            datesAndExcesses.put(ld, datesAndCalories.get(ld) > caloriesPerDay);
            executorService.execute(() -> {
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                            meal.getCalories(), datesAndExcesses.get(ld)));
                }
                incrementFinishedThreads(finishedThreads);
            });
            countDownLatch.countDown();
        }

        executorService.shutdown();
        while (finishedThreads.get() != meals.size()) {
        }
        return new ArrayList<>(result);
    }

    public static List<UserMealWithExcess> filteredBySingleStream(List<UserMeal> meals, LocalTime startTime,
                                                                  LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.toList()))
                .entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getValue().stream()
                        .mapToInt(UserMeal::getCalories).sum() > caloriesPerDay, Map.Entry::getValue))
                .entrySet().stream()
                .map(entry -> entry.getValue().stream()
                        .filter(meal ->
                                TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                        .map(meal -> new UserMealWithExcess(meal.getDateTime(),
                                meal.getDescription(), meal.getCalories(), entry.getKey()))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

    }

    private static synchronized void incrementFinishedThreads(AtomicInteger atomicInteger) {
        atomicInteger.set(atomicInteger.incrementAndGet());
    }
}