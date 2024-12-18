package com.jasmine.quizzy;

public class SessionManager {
    private static int currentUserId = -1;
    private static String currentUsername = null;

    // Method to set the current user ID and username after a successful login
    public static void setCurrentUser(int userId, String username) {
        currentUserId = userId;
        currentUsername = username;
    }

    // Method to get the current user ID
    public static int getCurrentUserId() {
        if (currentUserId == -1) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return currentUserId;
    }

    // Method to get the current username
    public static String getCurrentUsername() {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return currentUsername;
    }

    // Method to clear the user session (e.g., during logout)
    public static void logout() {
        currentUserId = -1;
        currentUsername = null;
    }
}
