package com.jasmine.quizzy;

import com.jasmine.quizzy.ButtonStyler;
import com.jasmine.quizzy.SoundEffect;
import com.jasmine.quizzy.StyleUtil;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class Settings {

    private static double volum = 0.5; // Default value

    public static double getVolum() {
        return volum;
    }

    public static void setVolum(double v) {
        volum = v;
    }

    public void start(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34); // Adjusted font size
        Font fontText = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24); // Adjusted font size

        // Title Label
        Label settingsLabel = new Label("Settings");
        settingsLabel.setFont(customFont);
        settingsLabel.setTextFill(Color.web("#ffffff"));
        settingsLabel.setMouseTransparent(true); // Prevents blocking clicks

        // User info
        Label userEmailLabel = new Label("Email: ");
        userEmailLabel.setFont(fontText);
        userEmailLabel.setTextFill(Color.web("#ffffff"));  // Set the label text color to white

        TextField userEmailField = new TextField();
        userEmailField.setPrefSize(300, 40); // Match the example styling
        userEmailField.setMaxWidth(300);
        StyleUtil.styleTextField(userEmailField);  // Apply styling from StyleUtil
        userEmailField.setPromptText("Enter new email");

        Label passwordLabel = new Label("Password: ");
        passwordLabel.setFont(fontText);
        passwordLabel.setTextFill(Color.web("#ffffff"));  // Set the label text color to white

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300, 40); // Match the example styling
        passwordField.setMaxWidth(300);
        StyleUtil.styleTextField(passwordField);  // Apply styling from StyleUtil
        passwordField.setPromptText("Enter new password");

        // Create buttons
        Button saveButton = new Button("Save Changes");
        Button logoutButton = new Button("Log Out");
        Button backButton = new Button("Go Back");

        // Apply button styles
        ButtonStyler.applyButtonStyles(saveButton);
        ButtonStyler.applyButtonStyles(logoutButton);
        ButtonStyler.applyButtonStyles(backButton);

        // Set button sizes
        saveButton.setPrefSize(200, 50);
        logoutButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });


        saveButton.setOnAction(e -> {
            String email = userEmailField.getText();
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
        ButtonStyler.applyButtonStyles(goBackButton);
        goBackButton.setPrefWidth(178);
        goBackButton.setAlignment(Pos.CENTER);

        goBackButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Action for goBackButton
        goBackButton.setOnAction(e -> {
            try {
                new MainMenu("User").show(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        logoutButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Log Out Button Action
        logoutButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            // Show confirmation dialog
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Clear session and redirect to login
                    SessionManager.logout();

                    try {
                        new LoginForm().show(primaryStage); // Adjust this to your login form method
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        });

        // Go Back Button Action
        backButton.setOnAction(event -> {
            // Go back to the main menu or previous screen
            SoundEffect.playSound("/buttonPress.mp3");
            String username = SessionManager.getCurrentUsername();
            // Play sound effect on go back
            new MainMenu(username).show(primaryStage);  // Go back to the main menu (example)
        });

        // Volume Label
        Label volumeLabel = new Label("Adjust Volume:");
        volumeLabel.setTextFill(Color.web("#ffffff"));
        volumeLabel.setFont(fontText);  // Custom font

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

        // Layout for fields and buttons
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                settingsLabel,
                userEmailLabel, userEmailField,
                passwordLabel, passwordField,
                volumeLabel, volumeSlider,
                saveButton, backButton, logoutButton
        );

        // Apply background
        layout.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
