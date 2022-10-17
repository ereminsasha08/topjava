package ru.javawebinar.topjava.controller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryRepository;
import ru.javawebinar.topjava.servicees.Service;
import ru.javawebinar.topjava.servicees.ServiceImp;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;

    private static Service service = new ServiceImp(new InMemoryRepository());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = String.valueOf(request.getParameter("action"));

        switch (action) {
            case ("edit"): {
                String strId = String.valueOf(request.getParameter("mealId"));
                log.debug("edit meal {}", strId);
                Meal meal = service.getMeal(Integer.parseInt(strId));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/editOrCreateMeal.jsp").forward(request, response);
                response.sendRedirect("meals");
                break;
            }
            case ("create"): {
                log.debug("creat meal");
                request.getRequestDispatcher("/editOrCreateMeal.jsp").forward(request, response);
                response.sendRedirect("meals");
                break;
            }
            case ("delete"): {
                Integer id = Integer.parseInt(String.valueOf(request.getParameter("mealId")));
                log.debug("delete meal {}", id);
                service.deleteMeal(id);
                response.sendRedirect("meals");
                break;
            }
            case ("null"): {
                log.debug("forward to meals");
                List<MealTo> mealToList = MealsUtil.allMeals(service.getAllMeal(), CALORIES_PER_DAY);
                request.setAttribute("mealToList", mealToList);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            log.debug("create meal");
            String description = request.getParameter("description");
            String calories = request.getParameter("calories");
            String dateTime = request.getParameter("dataTime");
            if (!(description.equals("") || calories.equals(""))) {
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
                service.createMeal(localDateTime, description, Integer.parseInt(calories));
            }
        } else {
            log.debug("update meal");
            Meal meal = service.getMeal(Integer.parseInt(mealId));
            String description = new String(request.getParameter("description").getBytes("ISO-8859-1"), "UTF-8");
            description = description.equals("") ? meal.getDescription() : description;
            String calories = request.getParameter("calories");
            calories = calories.equals("") ? String.valueOf(meal.getCalories()) : calories;
            String dateTime = request.getParameter("dataTime");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
            service.updateMeal(Integer.parseInt(mealId), localDateTime, description, Integer.parseInt(calories));
        }
        log.debug("forward to meals");
        List<MealTo> mealToList = MealsUtil.allMeals(service.getAllMeal(), CALORIES_PER_DAY);
        request.setAttribute("mealToList", mealToList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

}
