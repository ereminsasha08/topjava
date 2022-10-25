package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), USER_ID));
    }

    @Test
    public void getNotFoundException() {
        assertThrows(NotFoundException.class, () -> mealService.get(1, NOT_FOUND));
    }

    @Test
    public void deleteNotFoundException() {
        mealService.delete(1, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(1, NOT_FOUND));
    }
    @Test
    public void get() {
        Meal meal = mealService.get(1, USER_ID);
        assertThat(meal1).isEqualTo(meal);
    }

    @Test
    public void delete() {
        mealService.delete(1, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(1, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate =  LocalDate.of(2020, Month.JANUARY, 30);
        List<Meal> mealsFilterAndSorted = meals.stream()
                .filter(o1 -> o1.getDateTime().isAfter(DateTimeUtil.atStartOfDayOrMin(startDate)))
                .sorted((o1, o2) -> o1.getDateTime().isAfter(o2.getDateTime()) ? -1 : 0)
                .collect(Collectors.toList());
        assertThat(mealsFilterAndSorted).isEqualTo(mealService.getBetweenInclusive(startDate, LocalDate.now(), USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> mealsSorted = meals.stream().sorted((o1, o2) -> o1.getDateTime().isAfter(o2.getDateTime()) ? -1 : 0).collect(Collectors.toList());
        assertThat(mealsSorted).isEqualTo(mealService.getAll(USER_ID));
    }
    @Test
    public void updateNotFoundException() {

       assertThrows(NotFoundException.class, () -> mealService.update(getNew(), NOT_FOUND));

    }

    @Test
    public void update() {
        Meal updateMeal = meal1;
        mealService.update(updateMeal, USER_ID);
        assertThat(meal1).isEqualTo(mealService.get(1, USER_ID));
    }

    @Test
    public void create() {
        Meal createMeal = mealService.create(getNew(), USER_ID);
        Integer id = createMeal.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(id);
        assertThat(newMeal).isEqualTo(createMeal);
    }
}