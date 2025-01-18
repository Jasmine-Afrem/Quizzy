package com.jasmine.quizzy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class manages the session information for the current user, including user ID, username, and score.
 * It provides methods for getting and setting the current user's session data.
 */
public class SessionManager {
    private static int currentUserId = -1;
    private static String currentUsername = null;
    private static int currentUserScore = 0;  // Variable to store the user's score

    /**
     * Sets the current user ID and username after a successful login.
     * @param userId The unique ID of the user.
     * @param username The username of the user.
     */
    public static void setCurrentUser(int userId, String username) {
        currentUserId = userId;
        currentUsername = username;
    }

    /**
     * Gets the current user ID.
     * @return The current user ID.
     * @throws IllegalStateException If no user is logged in.
     */
    public static int getCurrentUserId() {
        if (currentUserId == -1) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return currentUserId;
    }

    /**
     * Gets the current username.
     * @return The current username.
     * @throws IllegalStateException If no user is logged in.
     */
    public static String getCurrentUsername() {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return currentUsername;
    }

    /**
     * Gets the current user's score from a file called "scor.txt".
     * The score is read from the file based on the current username.
     * @return The current user's score.
     */
    static int getCurrentUserScore() {
        File file = new File("scor.txt");
        String currentUsername = SessionManager.getCurrentUsername().trim();  // Trim any extra spaces
        System.out.println("Current username: " + currentUsername);

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim(); // Trim whitespace

                    if (line.contains(":")) { // Validate line format
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

    /**
     * Updates the current user's score.
     * @param score The new score to set for the current user.
     */
    public static void setCurrentUserScore(int score) {
        currentUserScore = score;
    }

    /**
     * Clears the user session (e.g., during logout).
     * Resets the user ID, username, and score to their default values.
     */
    public static void logout() {
        currentUserId = -1;
        currentUsername = null;
        currentUserScore = 0;  // Reset score when logging out
    }
}
