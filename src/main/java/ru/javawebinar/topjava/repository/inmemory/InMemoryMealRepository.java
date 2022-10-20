package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.user.AbstractUserController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            log.info("save {}", meal);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (meal.getUserId() == SecurityUtil.authUserId()) {
            log.info("update {}", meal);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        else
            return null;
    }

    @Override
    public boolean delete(int id) {
        if (ValidationUtil.identityMealUserId(repository.get(id))) {
            log.info("delete {}", id);
            return repository.remove(id) != null;
        }
        else
            return false;
    }

    @Override
    public Meal get(int id) {
        if (ValidationUtil.identityMealUserId(repository.get(id))) {
            log.info("get {}", id);
            return repository.get(id);
        }
        else
            return null;
    }

    @Override
    public Collection<Meal> getAll(LocalDate withDate, LocalDate beforeDate) {
        log.info("getAll");
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == SecurityUtil.authUserId())
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalDate(), withDate, beforeDate))
                .sorted((o1, o2) -> {
                    if (o1.getDateTime().isAfter(o2.getDateTime()))
                    return -1;
                    else
                       return 0;
                }).collect(Collectors.toList());
    }
}

