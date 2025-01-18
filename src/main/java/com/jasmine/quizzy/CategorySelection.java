package com.jasmine.quizzy;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

/**
 * The {@code CategorySelection} class represents the category selection screen for the quiz application.
 * It allows the user to choose a quiz category (Science, History, Geography, or Art) and start the quiz.
 * The screen includes a title, buttons for each category, and a back button to return to the main menu.
 */
public class CategorySelection {

    /**
     * Displays the category selection screen.
     * This method sets up the title, buttons, layout, background image, and applies button styles.
     * It also handles the interaction with buttons to start the quiz for each category or return to the main menu.
     *
     * @param primaryStage the main stage of the application
     */
    public void show(Stage primaryStage) {
        // Load custom font for the title
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

        // Apply styles to buttons using ButtonStyler
        ButtonStyler.applyButtonStyles(scienceButton);
        ButtonStyler.applyButtonStyles(historyButton);
        ButtonStyler.applyButtonStyles(geographyButton);
        ButtonStyler.applyButtonStyles(artButton);
        ButtonStyler.applyButtonStyles(backButton);

        // Set preferred size for buttons
        scienceButton.setPrefSize(200, 50);
        historyButton.setPrefSize(200, 50);
        geographyButton.setPrefSize(200, 50);
        artButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        // Add sound effects to buttons
        addButtonSoundEffect(scienceButton, historyButton, geographyButton, backButton);

        // Set actions for the buttons to start quizzes or navigate to the main menu
        scienceButton.setOnAction(event -> startQuiz(primaryStage, "Science"));
        historyButton.setOnAction(event -> startQuiz(primaryStage, "History"));
        geographyButton.setOnAction(event -> startQuiz(primaryStage, "Geography"));
        artButton.setOnAction(actionEvent -> startQuiz(primaryStage, "Art"));
        backButton.setOnAction(event -> new MainMenu("User").show(primaryStage));

        // Create layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, scienceButton, historyButton, geographyButton, artButton, backButton);

        // Load background image and apply it to the scene
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/background.jpg")).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Create a dark purple panel to hold the layout with shadow effect
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 500);
        darkPurplePanel.setMaxWidth(400);
        darkPurplePanel.setMaxHeight(500);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));

        // Add layout to the dark purple panel
        darkPurplePanel.getChildren().add(layout);

        // Set up root layout with background image and panel
        StackPane root = new StackPane();
        root.setBackground(new Background(background));
        root.getChildren().add(darkPurplePanel);

        // Set the scene and show the stage
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Adds a sound effect to each button when pressed.
     * This method plays a "button press" sound whenever any of the passed buttons are clicked.
     *
     * @param buttons the buttons to which the sound effect should be applied
     */
    private void addButtonSoundEffect(Button... buttons) {
        for (Button button : buttons) {
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));
        }
    }

    /**
     * Starts the quiz for the selected category.
     * This method shows an alert informing the user that the game is about to start, then starts the quiz after a small delay.
     *
     * @param primaryStage the main stage of the application
     * @param category the category of the quiz to be started (e.g., Science, History, Geography, Art)
     */
    public void startQuiz(Stage primaryStage, String category) {
        // Show the "Play" alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Play");
        alert.setHeaderText(null);
        alert.setContentText("The game is about to start!");
        alert.show();

        // Create a small delay before starting the quiz
        PauseTransition pause = new PauseTransition(Duration.millis(1));

        // Close the alert and start the quiz after the pause
        pause.setOnFinished(event -> {
            alert.close();  // Close the alert
            try {
                // Start the quiz with the selected category
                new Play(category).start(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pause.play();
    }
}