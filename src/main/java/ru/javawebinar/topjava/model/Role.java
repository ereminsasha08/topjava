package ru.javawebinar.topjava.model;

import java.util.HashSet;
import java.util.Set;

public enum Role {

    USER,
    ADMIN;

    public static Role[] valueOf(Role... roles) {
        return roles;
    }

}