package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The {@code Main} class serves as the entry point for the Quizzy application.
 * It initializes the application, starts background music, and displays the first page.
 */
public class Main extends Application {

    /**
     * Singleton instance of the {@code Main} class.
     */
    public static Main instance;

    /**
     * The username of the currently logged-in user.
     */
    public static String userName;

    /**
     * The {@code BackgroundMusic} instance for playing background music throughout the application.
     */
    public static BackgroundMusic backgroundMusic;

    /**
     * The main entry point for the JavaFX application.
     * This method initializes the primary stage, sets up background music, and displays the first page of the application.
     *
     * @param primaryStage The primary stage (window) for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primaryStage.setTitle("Quizzy");

        // Initialize background music with a specified audio file and volume
        backgroundMusic = new BackgroundMusic("/sounds/backgroundMusic.mp3");
        backgroundMusic.setVolume(0.3);
        backgroundMusic.play();

        // Display the first page
        showFirstPage(primaryStage);
    }

    /**
     * Displays the first page of the application.
     * This page is the entry point for users and provides an option to transition to the login form.
     *
     * @param primaryStage The primary stage (window) for the application.
     */
    private void showFirstPage(Stage primaryStage) {
        // Create an instance of FirstPage and display it
        FirstPage firstPage = new FirstPage();
        firstPage.show(primaryStage);
    }

    /**
     * The main method serves as the entry point for launching the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}