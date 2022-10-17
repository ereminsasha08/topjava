package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository implements Repository{
    public static ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    static {
        meals.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
    AtomicInteger atomicInteger = new AtomicInteger(8);
    @Override
    public Meal addMeal(LocalDateTime localDateTime, String description, int calories) {
        return meals.put(atomicInteger.incrementAndGet(), new Meal(atomicInteger.get(), localDateTime, description, calories));
    }

    @Override
    public Meal getMeal(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAllMeal() {
        return meals.values().stream().toList();
    }

    @Override
    public Meal updateMeal(int id, LocalDateTime localDateTime, String description, int calories) {
        return meals.replace(id, new Meal(id, localDateTime, description, calories));
    }

    @Override
    public boolean deleteMeal(int id) {
        return meals.remove(id) != null;
    }
}
