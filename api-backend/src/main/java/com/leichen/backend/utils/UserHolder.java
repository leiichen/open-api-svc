package com.leichen.backend.utils;

import com.leichen.backend.model.BO.UserBO;

public class UserHolder {
    private static final ThreadLocal<UserBO> userThreadLocal = new ThreadLocal<>();
    public static void saveUser(UserBO userBO) {
        userThreadLocal.set(userBO);
    }
    public static UserBO getUser() {
        return userThreadLocal.get();
    }
    public static void removeUser() {
        userThreadLocal.remove();
    }
}
