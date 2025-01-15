package com.jasmine.quizzy;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class CategorySelection {

    public void show(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 28);

        // Create a title for the category selection screen
        Label titleLabel = new Label("Select a Quiz Category");
        titleLabel.setFont(customFont);
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setAlignment(Pos.CENTER);

        // Create buttons for categories
        Button scienceButton = new Button("Science");
        Button historyButton = new Button("History");
        Button geographyButton = new Button("Geography");
        Button artButton = new Button("Art");
        Button backButton = new Button("Back");

        // Apply styles to buttons
        ButtonStyler.applyButtonStyles(scienceButton);
        ButtonStyler.applyButtonStyles(historyButton);
        ButtonStyler.applyButtonStyles(geographyButton);
        ButtonStyler.applyButtonStyles(artButton);
        ButtonStyler.applyButtonStyles(backButton);

        scienceButton.setPrefSize(200, 50);
        historyButton.setPrefSize(200, 50);
        geographyButton.setPrefSize(200, 50);
        artButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        // Add sound effects
        addButtonSoundEffect(scienceButton, historyButton, geographyButton, backButton);

        // Button actions
        scienceButton.setOnAction(event -> startQuiz(primaryStage, "Science"));
        historyButton.setOnAction(event -> startQuiz(primaryStage, "History"));
        geographyButton.setOnAction(event -> startQuiz(primaryStage, "Geography"));
        artButton.setOnAction(actionEvent -> startQuiz(primaryStage, "Art"));
        backButton.setOnAction(event -> new MainMenu("User").show(primaryStage));

        // Layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, scienceButton, historyButton, geographyButton, artButton, backButton);

        // Set background
        layout.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setScene(scene);
    }

    private void addButtonSoundEffect(Button... buttons) {
        for (Button button : buttons) {
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));
        }
    }

    public void startQuiz(Stage primaryStage, String category) {
        // Show the initial "Play" alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Play");
        alert.setHeaderText(null);
        alert.setContentText("The game is about to start!");
        alert.show();

        // Create a pause transition to delay the quiz start
        PauseTransition pause = new PauseTransition(Duration.millis(1));

        // When the pause ends, we close the alert and show the category selection
        pause.setOnFinished(event -> {
            // Close the alert before transitioning


            // Start the actual quiz
            try {
                new Play(category).start(primaryStage);  // Starts the quiz
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            alert.close();
        });

        pause.play();
    }

}
