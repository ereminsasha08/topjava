package ru.javawebinar.topjava.servicees;

import ru.javawebinar.topjava.repository.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceImp implements Service {
    private final Repository repository;

    public ServiceImp(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Meal createMeal(LocalDateTime localDateTime, String description, int calories) {
        return repository.addMeal(localDateTime, description, calories);
    }

    @Override
    public Meal getMeal(int id) {
        return repository.getMeal(id);
    }

    @Override
    public List<Meal> getAllMeal() {
        return repository.getAllMeal();
    }

    @Override
    public Meal updateMeal(int id, LocalDateTime localDateTime, String description, int calories) {
        return repository.updateMeal(id, localDateTime, description, calories);
    }

    @Override
    public boolean deleteMeal(int id) {
        repository.deleteMeal(id);
        return true;
    }
}
