package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @Autowired
    MealService mealService;


    @GetMapping()
    public String get(Model model,
                      @RequestParam @Nullable String action,
                      @RequestParam @Nullable String startDate,
                      @RequestParam @Nullable String startTime,
                      @RequestParam @Nullable String endDate,
                      @RequestParam @Nullable String endTime) {
        List<MealTo> mealTos;
        if ("filter".equals(action)) {
            log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, authUserId());
            List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(parseLocalDate(startDate), parseLocalDate(endDate), authUserId());
            mealTos = MealsUtil.getFilteredTos(mealsDateFiltered, authUserCaloriesPerDay(), parseLocalTime(startTime), parseLocalTime(endTime));
        } else {
            log.info("getAll for user {}", authUserId());
            List<Meal> meals = mealService.getAll(authUserId());
            mealTos = getTos(meals, authUserCaloriesPerDay());
        }
        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteId(@PathVariable int id) {
        log.info("delete meal {} for user {}", id, authUserId());
        mealService.delete(id, authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/mealForm")
    public String redirectToMealFormForCreate(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/mealForm/{id}")
    public String redirectToMealFormForCreate(Model model, @PathVariable String id) {
        log.info("get meal {} for user {}", id, authUserId());
        Meal meal = mealService.get(Integer.parseInt(id), authUserId());
        meal.setDescription("");
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/mealForm")
    public String create(String dateTime, String description, String calories) {

        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        log.info("create {} for user {}", meal, authUserId());
        mealService.create(meal, authUserId());
        return "redirect:/meals";
    }

    @PostMapping("/mealForm/{id}")
    public String update(String id, String dateTime, String description, String calories) {
        String paramId = Objects.requireNonNull(id);
        Meal meal = new Meal(Integer.parseInt(paramId), LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        log.info("update {} for user {}", meal, authUserId());
        mealService.update(meal, authUserId());
        return "redirect:/meals";
    }


}
