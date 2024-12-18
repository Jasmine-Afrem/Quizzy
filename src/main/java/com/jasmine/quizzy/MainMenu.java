package com.jasmine.quizzy;

import com.jasmine.quizzy.ButtonStyler;
import com.jasmine.quizzy.Settings;
import com.jasmine.quizzy.SoundEffect;
import com.jasmine.quizzy.StyleUtil;
import com.jasmine.quizzy.SessionManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenu {

    private final String userName;

    public MainMenu(String userName) {
        this.userName = SessionManager.getCurrentUsername();
    }

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

        // Apply styles to buttons
        ButtonStyler.applyButtonStyles(playButton);
        ButtonStyler.applyButtonStyles(settingsButton);
        ButtonStyler.applyButtonStyles(leaderboardButton);
        ButtonStyler.applyButtonStyles(quitButton);

        playButton.setPrefSize(200, 50);
        settingsButton.setPrefSize(200, 50);
        leaderboardButton.setPrefSize(200, 50);
        quitButton.setPrefSize(200, 50);

        settingsButton.setOnAction(event -> {
            new Settings().start(primaryStage);
        });

        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        settingsButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        leaderboardButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        quitButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        quitButton.setOnAction(event -> System.exit(0));

        // Add sound effects
        addButtonSoundEffect(playButton, settingsButton, leaderboardButton, quitButton);

        // Layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(welcomeLabel, playButton, settingsButton, leaderboardButton, quitButton);

        // Set background
        layout.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addButtonSoundEffect(Button... buttons) {
        for (Button button : buttons) {
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/buttonPress.mp3"));
        }
    }
}
