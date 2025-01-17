package com.jasmine.quizzy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SessionManager {
    private static int currentUserId = -1;
    private static String currentUsername = null;
    private static int currentUserScore = 0;  // New variable to store the user's score

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

    // Method to get the current user's score
    static int getCurrentUserScore() {
        File file = new File("scor.txt");
        String currentUsername = SessionManager.getCurrentUsername().trim();  // Trim any extra spaces
        System.out.println("Current username: " + currentUsername);

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim(); // Trim whitespace

                    if (!line.isEmpty() && line.contains(":")) { // Validate line format
                        String[] parts = line.split(":");

                        if (parts.length == 2) {
                            String username = parts[0].trim(); // Trim any extra spaces
                            String score = parts[1].trim();

                            // Exact match comparison (case-sensitive)
                            if (username.equals(currentUsername)) {
                                int scoreValue = Integer.parseInt(score);
                                SessionManager.setCurrentUserScore(scoreValue);  // Set score in SessionManager
                                return scoreValue;  // Return the score as an integer
                            }
                        }
                    }
                }

                // If no score is found for the current user
                System.out.println("No valid score found for the current user.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist.");
        }
        return 0; // Return 0 if no score is found for the user
    }

    // Method to update the current user's score
    public static void setCurrentUserScore(int score) {
        currentUserScore = score;
    }

    // Method to clear the user session (e.g., during logout)
    public static void logout() {
        currentUserId = -1;
        currentUsername = null;
        currentUserScore = 0;  // Reset score when logging out
    }
}
