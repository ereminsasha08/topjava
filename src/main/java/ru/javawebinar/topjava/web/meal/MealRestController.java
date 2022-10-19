package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(MealRestController.class);
    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    private MealService service;

    public List<MealTo> getAll(){
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(), SecurityUtil.authUserCaloriesPerDay());
    }

}