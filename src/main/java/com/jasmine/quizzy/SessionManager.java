package com.jasmine.quizzy;

public class SessionManager {
    private static int currentUserId = -1;

    // Method to set the current user ID after a successful login
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    // Method to get the current user ID
    public static int getCurrentUserId() {
        if (currentUserId == -1) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return currentUserId;
    }

    // Method to clear the user session (e.g., during logout)
    public static void logout() {
        currentUserId = -1;
    }
}

