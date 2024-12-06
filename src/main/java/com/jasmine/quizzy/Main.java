package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static Main instance;
    public static String userName;
    public static BackgroundMusic backgroundMusic;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primaryStage.setTitle("Quizzy");

        // Initialize background music
        backgroundMusic = new BackgroundMusic("/backgroundMusic.mp3");
        backgroundMusic.setVolume(0.3);
        backgroundMusic.play();

        // Start with the First Page
        showFirstPage(primaryStage);
    }

    private void showFirstPage(Stage primaryStage) {
        // Create an instance of FirstPage and show it
        FirstPage firstPage = new FirstPage();
        firstPage.show(primaryStage);

        // Add a listener to transition to LoginForm when the play button is clicked
        // You can set the transition to show LoginForm within the FirstPage class.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
