package ru.javawebinar.topjava.servicees;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface Service {
    public Meal createMeal(LocalDateTime localDateTime, String description, int calories);

    public Meal getMeal(int id);

    public List<Meal> getAllMeal();

    public Meal updateMeal(int id, LocalDateTime localDateTime, String description, int calories);

    public boolean deleteMeal(int id);


}
