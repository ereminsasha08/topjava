package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
        this.crudRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            meal.setUser(crudUserRepository.getReferenceById(userId));
            return crudRepository.save(meal);
        }
        if (get(meal.id(), userId) == null)
            return null;
        else {
            meal.setUser(crudUserRepository.getReferenceById(userId));
            return crudRepository.save(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUserAndSortTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllByUserAndTimeAndSortTimeDesc(startDateTime, endDateTime, userId);
    }

    @Transactional
    @Override
    public User getUserByMealId(int id, int userId) {
        User user = get(id, userId).getUser();
        user.getCaloriesPerDay();
        return user;
    }
}
