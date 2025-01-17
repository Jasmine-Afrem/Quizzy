package com.jasmine.quizzy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Scores {

    public void show(Stage primaryStage) {
        // Load the background image
        Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Create the outer panel behind the components to add depth
        Rectangle backgroundPanel = new Rectangle(1024, 900);  // Match the scene size
        backgroundPanel.setFill(Color.web("rgba(48, 25, 52, 0.7)")); // Deep violet with transparency
        backgroundPanel.setArcWidth(20);  // Rounded corners for depth effect
        backgroundPanel.setArcHeight(20);  // Rounded corners for depth effect

        // Create VBox layout and set the background image
        VBox layout = new VBox(20);
        layout.setBackground(new Background(background));
        layout.setAlignment(Pos.CENTER);  // Ensures all elements inside VBox are centered

        // Font for the labels
        Font customFontTitles = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);
        Font customFontScore = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Score Section
        Label scoreTitle = new Label("Score");
        scoreTitle.setFont(customFontTitles);
        scoreTitle.setTextFill(Color.web("#ffffff"));
        scoreTitle.setAlignment(Pos.CENTER);  // Center the score title

        // Create a rounded background panel for score title
        Rectangle scoreTitlePanel = new Rectangle(0, 0, 200, 50);
        scoreTitlePanel.setFill(Color.web("rgba(48, 25, 52, 0.7)")); // Deep violet with transparency
        scoreTitlePanel.setArcWidth(20);  // Rounded corners
        scoreTitlePanel.setArcHeight(20);  // Rounded corners

        // Load the current user's score from SessionManager
        int userScoreValue = SessionManager.getCurrentUserScore();  // Get the score from SessionManager

        // Create the label for the score
        Label userScore = new Label("Your score is: " + userScoreValue + "!");
        userScore.setFont(customFontScore);
        userScore.setTextFill(Color.web("#ffffff"));
        userScore.setAlignment(Pos.CENTER);  // Center the user score

        // Rank Section
        Label rankTitle = new Label("Rank");
        rankTitle.setFont(customFontTitles);
        rankTitle.setTextFill(Color.web("#ffffff"));
        rankTitle.setAlignment(Pos.CENTER);  // Center the rank title

        String userRank = calculateRank(userScoreValue);
        Label userRankLabel = new Label(userRank);
        userRankLabel.setFont(customFontScore);
        userRankLabel.setTextFill(Color.web("#ffffff"));
        userRankLabel.setAlignment(Pos.CENTER);  // Center the user rank

        // Ranks Section
        Label ranksTitle = new Label("Ranks");
        ranksTitle.setFont(customFontTitles);
        ranksTitle.setTextFill(Color.web("#ffffff"));
        ranksTitle.setAlignment(Pos.CENTER);  // Center the ranks title

        // Rank details text as separate labels
        Label rankNoob = new Label("Noob: 0-20");
        Label rankBeginner = new Label("Beginner: 20-60");
        Label rankIntermediate = new Label("Intermediate: 60-100");
        Label rankMaster = new Label("Master: 100-200");
        Label rankEinstein = new Label("Einstein: 200+");

        // Set font and text properties for each label
        rankNoob.setFont(customFontScore);
        rankBeginner.setFont(customFontScore);
        rankIntermediate.setFont(customFontScore);
        rankMaster.setFont(customFontScore);
        rankEinstein.setFont(customFontScore);

        rankNoob.setTextFill(Color.web("#ffffff"));
        rankBeginner.setTextFill(Color.web("#ffffff"));
        rankIntermediate.setTextFill(Color.web("#ffffff"));
        rankMaster.setTextFill(Color.web("#ffffff"));
        rankEinstein.setTextFill(Color.web("#ffffff"));

        // Align all rank labels to the center
        rankNoob.setAlignment(Pos.CENTER);
        rankBeginner.setAlignment(Pos.CENTER);
        rankIntermediate.setAlignment(Pos.CENTER);
        rankMaster.setAlignment(Pos.CENTER);
        rankEinstein.setAlignment(Pos.CENTER);

        // Create a VBox container to hold all rank labels
        VBox rankDetailsContainer = new VBox(10); // 10 is the space between rank labels
        rankDetailsContainer.setAlignment(Pos.CENTER); // Ensure the rank labels are centered
        rankDetailsContainer.getChildren().addAll(
                rankNoob, rankBeginner, rankIntermediate, rankMaster, rankEinstein);

        // Create a rounded background panel for the rank details
        Rectangle rankPanel = new Rectangle(0, 0, 200, 200);
        rankPanel.setFill(Color.web("rgba(48, 25, 52, 0.7)")); // Deep violet with transparency
        rankPanel.setArcWidth(20);  // Rounded corners
        rankPanel.setArcHeight(20);  // Rounded corners

        // Add all elements to the layout (VBox)
        layout.getChildren().addAll(
                scoreTitle,
                userScore,
                rankTitle,
                userRankLabel,
                ranksTitle,
                rankDetailsContainer // Add rank details below the ranks title
        );

        // Add a button to go back to the main menu
        Button backButton = new Button("Back");
        ButtonStyler.applyButtonStyles(backButton);  // Assuming ButtonStyler is defined elsewhere
        backButton.setOnAction(e -> {
            try {
                new MainMenu(SessionManager.getCurrentUsername()).show(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add the back button to layout
        layout.getChildren().add(backButton);

        // Create a StackPane to put the background panel behind the VBox layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundPanel, layout);  // Background panel first, layout on top

        // Set the scene and show the stage
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String calculateRank(int score) {
        if (score >= 0 && score <= 20) {
            return "Noob";
        } else if (score > 20 && score <= 60) {
            return "Beginner";
        } else if (score > 60 && score <= 100) {
            return "Intermediate";
        } else if (score > 100 && score <= 200) {
            return "Master";
        } else {
            return "Einstein";
        }
    }
}
