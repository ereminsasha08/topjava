package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Role;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static Role role = Role.ADMIN;
    private static volatile int authUserId = 1;
    public static Role getRole(){
        return role;
    }
    public static int authUserId() {
        return authUserId;
    }
    public static void setAuthUserId(int id){
        authUserId = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}