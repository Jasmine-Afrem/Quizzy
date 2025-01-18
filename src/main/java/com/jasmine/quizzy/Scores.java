package com.jasmine.quizzy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * This class handles the display of the score and rank information
 * after the user finishes a quiz.
 */
public class Scores {

    /**
     * Displays the score, rank, and ranks list on the provided Stage.
     * @param primaryStage The main stage where the content will be displayed.
     */
    public void show(Stage primaryStage) {
        // Load the background image
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/background.jpg")).toExternalForm());

        // Create a BackgroundSize object (to scale the image appropriately)
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);

        // Create a BackgroundImage object
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Root container with background image
        StackPane root = new StackPane();
        root.setBackground(new Background(background));

        // Font setup
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);
        Font textFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Score section
        int userScoreValue = SessionManager.getCurrentUserScore();
        VBox scoreSection = createSection("Score", "Your score is: " + userScoreValue + "!", titleFont, textFont);

        // Rank section
        String userRank = calculateRank(userScoreValue);
        VBox rankSection = createSection("Rank", userRank, titleFont, textFont);

        // Ranks list section
        VBox ranksListSection = new VBox(10);
        ranksListSection.setAlignment(Pos.CENTER);
        ranksListSection.setPrefWidth(300);
        ranksListSection.setStyle("-fx-background-color: #4f2a70; -fx-background-radius: 20;");

        // Apply darker shadow effect to the ranks list section
        ranksListSection.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));  // Darker gray shadow

        Label ranksTitle = new Label("Ranks");
        ranksTitle.setFont(titleFont);
        ranksTitle.setTextFill(Color.WHITE);

        Label rankNoob = new Label("Noob: 0-20");
        Label rankBeginner = new Label("Beginner: 20-60");
        Label rankIntermediate = new Label("Intermediate: 60-100");
        Label rankMaster = new Label("Master: 100-200");
        Label rankEinstein = new Label("Einstein: 200+");

        rankNoob.setFont(textFont);
        rankBeginner.setFont(textFont);
        rankIntermediate.setFont(textFont);
        rankMaster.setFont(textFont);
        rankEinstein.setFont(textFont);

        rankNoob.setTextFill(Color.WHITE);
        rankBeginner.setTextFill(Color.WHITE);
        rankIntermediate.setTextFill(Color.WHITE);
        rankMaster.setTextFill(Color.WHITE);
        rankEinstein.setTextFill(Color.WHITE);

        ranksListSection.getChildren().addAll(
                ranksTitle, rankNoob, rankBeginner, rankIntermediate, rankMaster, rankEinstein);

        // Create a dark purple panel to hold the layout
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 500); // Adjust the size as needed
        darkPurplePanel.setMaxWidth(400); // Set max width for the panel
        darkPurplePanel.setMaxHeight(500); // Set max height for the panel
        darkPurplePanel.setStyle("-fx-background-color: #643b87; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));  // Darker gray shadow

        // Add the sections to the dark purple panel
        darkPurplePanel.getChildren().addAll(scoreSection, rankSection, ranksListSection);

        // Back button with hover effect
        Button backButton = new Button("Back");
        ButtonStyler.applyButtonStyles(backButton);
        backButton.setOnAction(e -> {
            try {
                new MainMenu(SessionManager.getCurrentUsername()).show(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Create a VBox to stack sections and the back button within the dark purple panel
        VBox darkPurpleContent = new VBox(20);
        darkPurpleContent.setAlignment(Pos.CENTER);
        darkPurpleContent.getChildren().addAll(scoreSection, rankSection, ranksListSection, backButton);

        // Add the content to the dark purple panel
        darkPurplePanel.getChildren().setAll(darkPurpleContent);

        // Add everything to the root container
        root.getChildren().add(darkPurplePanel);

        // Scene setup
        Scene scene = new Scene(root, 1024, 900); // Adjusted scene size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a section with a title and content.
     * @param titleText The title of the section.
     * @param contentText The content of the section.
     * @param titleFont The font for the title.
     * @param textFont The font for the content.
     * @return A VBox containing the title and content labels.
     */
    private VBox createSection(String titleText, String contentText, Font titleFont, Font textFont) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPrefWidth(250); // Adjusted for smaller sections
        section.setStyle("-fx-background-color: #4f2a70; -fx-background-radius: 15;");

        // Apply darker shadow effect to the section
        section.setEffect(new javafx.scene.effect.DropShadow(8, Color.DARKGRAY));  // Darker gray shadow

        Label title = new Label(titleText);
        title.setFont(titleFont);
        title.setTextFill(Color.WHITE);

        Label content = new Label(contentText);
        content.setFont(textFont);
        content.setTextFill(Color.WHITE);

        section.getChildren().addAll(title, content);

        return section;
    }

    /**
     * Determines the rank based on the user's score.
     * @param score The user's score.
     * @return A string representing the user's rank.
     */
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