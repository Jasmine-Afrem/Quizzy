package com.jasmine.quizzy;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The {@code MainMenu} class represents the main menu screen of the Quizzy application.
 * It provides navigation to other parts of the application such as Play, Settings, Scores, and Quit.
 */
public class MainMenu {

    /**
     * The username of the currently logged-in user.
     */
    private final String userName;

    /**
     * Constructs a {@code MainMenu} instance.
     * Initializes the menu with the currently logged-in user's username.
     *
     * @param userName The username of the logged-in user.
     */
    public MainMenu(String userName) {
        this.userName = SessionManager.getCurrentUsername();
    }

    /**
     * Displays the main menu screen.
     * Includes navigation options for Play, Settings, Scores, and Quit.
     *
     * @param primaryStage The primary stage (window) for the application.
     */
    public void show(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);

        // Create a welcome label
        Label welcomeLabel = new Label("Welcome, " + userName + "!");
        welcomeLabel.setFont(customFont);
        welcomeLabel.setTextFill(Color.web("#ffffff"));
        welcomeLabel.setMouseTransparent(true); // Avoid blocking clicks

        // Create buttons for the main menu
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button leaderboardButton = new Button("Scores");
        Button quitButton = new Button("Quit");

        // Define actions for the buttons
        playButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Play");
            alert.setHeaderText(null);
            alert.setContentText("The game is about to start!");

            alert.show();

            PauseTransition pause = new PauseTransition(Duration.millis(1));
            pause.setOnFinished(event -> {
                new CategorySelection().show(primaryStage);
                alert.close();
            });
            pause.play();
        });

        settingsButton.setOnAction(event -> new Settings().start(primaryStage));

        leaderboardButton.setOnAction(e -> {
            new Scores().show(primaryStage);
            Platform.runLater(() -> CustomAlert.showAlert("Here you can see information about your score and rank."));
        });

        quitButton.setOnAction(event -> System.exit(0));

        // Apply sound effects to all buttons
        addButtonSoundEffect(playButton, settingsButton, leaderboardButton, quitButton);

        // Apply styles to the buttons
        ButtonStyler.applyButtonStyles(playButton);
        ButtonStyler.applyButtonStyles(settingsButton);
        ButtonStyler.applyButtonStyles(leaderboardButton);
        ButtonStyler.applyButtonStyles(quitButton);

        playButton.setPrefSize(200, 50);
        settingsButton.setPrefSize(200, 50);
        leaderboardButton.setPrefSize(200, 50);
        quitButton.setPrefSize(200, 50);

        // Create a VBox layout for the buttons and welcome label
        VBox buttonsLayout = new VBox(25, welcomeLabel, new Region(), playButton, settingsButton, leaderboardButton, quitButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        // Create a dark purple panel for the buttons
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 500);
        darkPurplePanel.setMaxWidth(400);
        darkPurplePanel.setMaxHeight(500);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));

        // Add the buttons layout to the dark purple panel
        darkPurplePanel.getChildren().add(buttonsLayout);

        // Set the background image for the main menu
        StackPane root = new StackPane();
        root.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Add the dark purple panel to the root layout
        root.getChildren().add(darkPurplePanel);

        // Create the scene and display it on the primary stage
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Adds a sound effect to the given buttons when they are pressed.
     *
     * @param buttons The buttons to which the sound effect will be added.
     */
    private void addButtonSoundEffect(Button... buttons) {
        for (Button button : buttons) {
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));
        }
    }
}