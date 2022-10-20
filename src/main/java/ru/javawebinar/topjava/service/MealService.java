package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    private MealRepository repository;

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Collection<MealTo> getAll(LocalDate withDate, LocalTime withTime, LocalDate beforeDate, LocalTime beforeTime) {

       Collection<MealTo> meals = MealsUtil.getTos(repository.getAll(withDate, beforeDate), SecurityUtil.authUserCaloriesPerDay());
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(),withTime, beforeTime))
                .collect(Collectors.toList());
    }
}