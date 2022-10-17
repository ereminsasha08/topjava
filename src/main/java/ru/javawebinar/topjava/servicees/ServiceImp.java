package ru.javawebinar.topjava.servicees;

import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceImp implements Service {
    private final Dao dao;

    public ServiceImp(Dao dao) {
        this.dao = dao;
    }

    @Override
    public Meal createMeal(LocalDateTime localDateTime, String description, int calories) {
        return dao.addMeal(localDateTime, description, calories);
    }

    @Override
    public Meal getMeal(int id) {
        return dao.getMeal(id);
    }

    @Override
    public List<Meal> getAllMeal() {
        return dao.getAllMeal();
    }

    @Override
    public Meal updateMeal(int id, LocalDateTime localDateTime, String description, int calories) {
        return dao.updateMeal(id, localDateTime, description, calories);
    }

    @Override
    public boolean deleteMeal(int id) {
        dao.deleteMeal(id);
        return true;
    }
}
