package com.aje.onemenu.classes;

public class UserId {
    private String userId;
    private static UserId instance = null;
    protected UserId() {
        // Exists only to defeat instantiation.
    }
    public static UserId getInstance() {
        if(instance == null) {
            instance = new UserId();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}