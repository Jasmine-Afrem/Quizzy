package com.jasmine.quizzy;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.jasmine.quizzy.SessionManager.getCurrentUserId;
import com.jasmine.quizzy.BackgroundMusic;


public class Settings {
    private static double volum = 0.5; // Default value

    public static double getVolum() {
        return volum;
    }

    public static void setVolum(double v) {
        volum = v;
    }

    // Method to style buttons with the provided template
    private void applyButtonStyles(Button button) {
        button.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18));
        button.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; \n"
                        + "-fx-text-fill: white; \n"
                        + "-fx-background-radius: 25; \n"
                        + "-fx-padding: 10px 30px; \n"
                        + "-fx-background-color: linear-gradient(to bottom, #6A4791, #4B0082); \n"
                        + "-fx-border-color: transparent; \n"
                        + "-fx-border-width: 2px; \n"
                        + "-fx-border-radius: 25; \n"
                        + "-fx-transition: all 0.3s ease-in-out; \n"
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");

        // Hover effect
        button.setOnMouseEntered(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #a56ceb, #6A4791); \n"
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.3s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // Mouse exit (reset the background)
        button.setOnMouseExited(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #6A4791, #4B0082); \n"
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.3s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // Pressed effect
        button.setOnMousePressed(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #8B008B, #6A4791); \n"
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.2s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // When mouse is released, reset back to hover state or normal
        button.setOnMouseReleased(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #FF1493, #6A4791); \n"
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.3s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });
    }

    // Start method to build the UI
    public void start(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);
        Font customFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 42);

        // Create VBox container
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 30;");

        // Title Label
        Label label = new Label("Settings");
        label.setTextFill(Color.web("#ffffff"));
        label.setFont(customFont2);

        // Email Label Styling
        Label emailLabel = new Label("Change Email:");
        emailLabel.setTextFill(Color.web("#ffffff"));
        emailLabel.setFont(customFont); // Custom font

        TextField emailField = new TextField();
        emailField.setPromptText("Enter new email");
        emailField.setFont(customFont);
        emailField.setStyle("-fx-font: 18 \"SourGummy-Medium\"; \n"
                + "-fx-text-fill: white; \n"
                + "-fx-background-color: #6A4791; \n"
                + "-fx-background-radius: 10; \n"
                + "-fx-padding: 12px; \n"
                + "-fx-border-color: #cccccc; \n"
                + "-fx-border-width: 2px; \n"
                + "-fx-border-radius: 10; \n");
        emailField.setMaxWidth(300);

        // Password Label Styling
        Label passwordLabel = new Label("Change Password:");
        passwordLabel.setTextFill(Color.web("#ffffff"));
        passwordLabel.setFont(customFont);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        passwordField.setFont(customFont);
        passwordField.setStyle("-fx-font: 18 \"SourGummy-Medium\"; \n"
                + "-fx-text-fill: white; \n"
                + "-fx-background-color: #6A4791; \n"
                + "-fx-background-radius: 10; \n"
                + "-fx-padding: 12px; \n"
                + "-fx-border-color: #cccccc; \n"
                + "-fx-border-width: 2px; \n"
                + "-fx-border-radius: 10; \n");
        passwordField.setMaxWidth(300);

        // Volume Label
        Label volumeLabel = new Label("Adjust Volume:");
        volumeLabel.setTextFill(Color.web("#ffffff"));
        volumeLabel.setFont(customFont);  // Custom font

        // Volume Slider
        Slider volumeSlider = new Slider(0, 1, volum); // Min value: 0, Max value: 1, Initial value: 0.5
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue();
            setVolum(volume); // Update the static volume
            Main.backgroundMusic.setVolume(volume); // Update the background music volume
            System.out.println("Volume set to: " + Main.backgroundMusic.getVolume());
        });

        volumeSlider.setBlockIncrement(0.05); // Block increment for finer control
        volumeSlider.setStyle("-fx-font: 18 \"SourGummy-Medium\"; \n"
                + "-fx-text-fill: white; \n"
                + "-fx-background-color: #6A4791; \n"
                + "-fx-background-radius: 10; \n"
                + "-fx-padding: 12px; \n"
                + "-fx-border-color: #cccccc; \n"
                + "-fx-border-width: 2px; \n"
                + "-fx-border-radius: 10; \n");

        volumeSlider.setMaxWidth(300);

        // Add listener to update the volume value when the slider is moved
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            setVolum(newValue.doubleValue());
            System.out.println("Volume set to: " + getVolum()); // Just to see the current volume value in the console
        });

        // Save Changes Button
        Button saveButton = new Button("Save Changes");
        applyButtonStyles(saveButton);

        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Add action to save the changes
        saveButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() && password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in at least one field (email or password).");
                alert.showAndWait();
                return;
            }

            // Simulate saving changes (or call your actual database saving logic here)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your settings have been saved.");
            alert.showAndWait();
        });

        // Go Back Button
        Button goBackButton = new Button("Go back");
        applyButtonStyles(goBackButton);
        goBackButton.setPrefWidth(178);
        goBackButton.setAlignment(Pos.CENTER);

        goBackButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Action for goBackButton
        goBackButton.setOnAction(e -> {
            try {
                Main.instance.showMainMenu(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add UI elements to the VBox
        vbox.getChildren().addAll(label, emailLabel, emailField, passwordLabel, passwordField, volumeLabel, volumeSlider, saveButton, goBackButton);

        // Set Background Image
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/background.jpg").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
        vbox.setBackground(new Background(backgroundImage));

        // Add the Logout Button
        Button logoutButton = new Button("Log Out");
        applyButtonStyles(logoutButton);

        // Make the button stand out with a brighter color
        logoutButton.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; \n"
                        + "-fx-text-fill: white; \n"
                        + "-fx-background-radius: 25; \n"
                        + "-fx-padding: 10px 30px; \n"
                        + "-fx-background-color: linear-gradient(to bottom, #7238a8, #9c62cc); \n"
                        + "-fx-border-color: transparent; \n"
                        + "-fx-border-width: 2px; \n"
                        + "-fx-border-radius: 25; \n"
                        + "-fx-transition: all 0.3s ease-in-out; \n"
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");

        // Hover effect for the Logout button
        logoutButton.setOnMouseEntered(event -> {
            logoutButton.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #683a94, #8f4fc4); \n" // Softer pink hover
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.3s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // Reset style on mouse exit
        logoutButton.setOnMouseExited(event -> {
            logoutButton.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; \n"
                            + "-fx-text-fill: white; \n"
                            + "-fx-background-radius: 25; \n"
                            + "-fx-padding: 10px 30px; \n"
                            + "-fx-background-color: linear-gradient(to bottom, #7238a8, #9c62cc); \n"
                            + "-fx-border-color: transparent; \n"
                            + "-fx-border-width: 2px; \n"
                            + "-fx-border-radius: 25; \n"
                            + "-fx-transition: all 0.3s ease-in-out; \n"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        logoutButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Action for Logout button
        logoutButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            // Show confirmation dialog
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Clear session and redirect to login
                    SessionManager.logout();

                    try {
                        Main.instance.showLoginForm(primaryStage); // Adjust this call to match your login form method
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        });

        // Add the logout button to the VBox
        vbox.getChildren().add(logoutButton);

        saveButton.setPrefWidth(200);
        goBackButton.setPrefWidth(150);
        logoutButton.setPrefWidth(150);

        // Create and set the Scene
        Scene scene = new Scene(vbox, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // This method should retrieve the user ID for the current logged-in user
    private int getUserId() {
        // Implement logic to get the user ID from the current session or logged-in user
        return 1; // Placeholder, replace with actual logic
    }
}