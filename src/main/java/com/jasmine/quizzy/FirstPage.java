package com.jasmine.quizzy;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The {@code FirstPage} class represents the first page of the application where users are presented with a logo and a "Play" button.
 * It handles the display of the page, animations, and navigation to the login form when the user clicks the "Play" button.
 */
public class FirstPage {

    // The "Play" button on the first page, made accessible for testing purposes
    public Button playButton;

    /**
     * Displays the first page of the application with a logo and a "Play" button.
     * The logo floats with a smooth animation, and when the "Play" button is clicked, the login form is shown.
     *
     * @param primaryStage The primary stage (window) of the application.
     */
    public void show(Stage primaryStage) {
        // Load the custom font for the page

        // Load and display the logo image
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/Quizzy.png")).toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(600);
        logoImageView.setPreserveRatio(true);

        // Set up a floating animation for the logo
        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(15); // Moves the logo vertically by 15 units
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE); // Infinite loop of the animation
        floatingAnimation.setAutoReverse(true); // Reverses the animation each time it completes
        floatingAnimation.play(); // Start the animation

        // Make the "Play" button accessible for testing
        playButton = new Button("Play");
        ButtonStyler.applyButtonStyles(playButton); // Apply custom styles to the button

        // Set the action for the "Play" button
        playButton.setOnAction(event -> {
            new LoginForm().show(primaryStage); // Show the login form when the button is clicked
        });

        // Add a sound effect for the button press
        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/sounds/buttonPress.mp3"); // Play sound when button is pressed
        });

        // Create the layout for the page (vertical box layout)
        VBox mainLayout = new VBox(30); // 30px spacing between elements
        mainLayout.getChildren().addAll(logoImageView, playButton); // Add the logo and the button to the layout
        mainLayout.setAlignment(Pos.CENTER); // Center the content

        // Set a background image for the page
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/background.jpg")).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        mainLayout.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));

        // Set up the scene and show the primary stage
        Scene scene = new Scene(mainLayout, 1024, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}