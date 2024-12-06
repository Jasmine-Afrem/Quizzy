package com.jasmine.quizzy;

import com.jasmine.quizzy.ButtonStyler;
import com.jasmine.quizzy.SoundEffect;
import com.jasmine.quizzy.StyleUtil;
import com.jasmine.quizzy.MainMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        Button backButton = new Button("Back");

        // Apply styles to buttons
        ButtonStyler.applyButtonStyles(scienceButton);
        ButtonStyler.applyButtonStyles(historyButton);
        ButtonStyler.applyButtonStyles(geographyButton);
        ButtonStyler.applyButtonStyles(backButton);

        scienceButton.setPrefSize(200, 50);
        historyButton.setPrefSize(200, 50);
        geographyButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        // Add sound effects
        addButtonSoundEffect(scienceButton, historyButton, geographyButton, backButton);

        // Button actions
        scienceButton.setOnAction(event -> startQuiz(primaryStage, "Science"));
        historyButton.setOnAction(event -> startQuiz(primaryStage, "History"));
        geographyButton.setOnAction(event -> startQuiz(primaryStage, "Geography"));
        backButton.setOnAction(event -> new MainMenu("User").show(primaryStage));

        // Layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, scienceButton, historyButton, geographyButton, backButton);

        // Set background
        layout.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setScene(scene);
    }

    private void addButtonSoundEffect(Button... buttons) {
        for (Button button : buttons) {
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/buttonPress.mp3"));
        }
    }

    private void startQuiz(Stage primaryStage, String category) {
        // Quiz logic for the selected category
        System.out.println("Starting quiz in category: " + category);
    }
}
