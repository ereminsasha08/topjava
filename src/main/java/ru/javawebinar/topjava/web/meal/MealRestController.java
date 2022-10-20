package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(MealRestController.class);
    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    private MealService service;

    public List<MealTo> getAll(LocalDate withDate, LocalTime withTime, LocalDate beforeDate, LocalTime beforeTime){
        log.info("getAll");
        withDate = withDate == null? LocalDate.MIN: withDate;
        withTime = withTime == null? LocalTime.MIN: withTime;
        beforeDate = beforeDate == null? LocalDate.MAX: beforeDate;
        beforeTime = beforeTime == null? LocalTime.MAX: beforeTime;
        List<MealTo> mealTos = service.getAll( withDate,  withTime,  beforeDate,  beforeTime).stream().collect(Collectors.toList());
        return mealTos;
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void add(Meal meal) {
        log.info("add {}", meal);
        service.create(meal);
    }
}