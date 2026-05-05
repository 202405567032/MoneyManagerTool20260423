package com.personalfinance.accountingsystem.util;

public class UserContextHolder {
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        currentUserId.set(userId);
    }

    public static Long getCurrentUserId() {
        Long id = currentUserId.get();
        if (id == null) throw new RuntimeException("No authenticated user found");
        return id;
    }

    public static void clear() {
        currentUserId.remove();
    }
}
