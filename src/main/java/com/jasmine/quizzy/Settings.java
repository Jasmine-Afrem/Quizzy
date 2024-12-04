package com.jasmine.quizzy;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        emailField.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; \n"
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
        passwordLabel.setFont(customFont);  // Custom font

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        passwordField.setFont(customFont);
        passwordField.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; \n"
                        + "-fx-text-fill: white; \n"
                        + "-fx-background-color: #6A4791; \n"
                        + "-fx-background-radius: 10; \n"
                        + "-fx-padding: 12px; \n"
                        + "-fx-border-color: #cccccc; \n"
                        + "-fx-border-width: 2px; \n"
                        + "-fx-border-radius: 10; \n");
        passwordField.setMaxWidth(300);

        // Save Changes Button
        Button saveButton = new Button("Save Changes");
        applyButtonStyles(saveButton);

        saveButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            // Ensure at least one field is filled
            if (email.isEmpty() && password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in at least one field (email or password).");
                alert.showAndWait();
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
                // Dynamically build the SQL query
                StringBuilder sql = new StringBuilder("UPDATE users SET ");
                List<Object> params = new ArrayList<>();

                if (!email.isEmpty()) {
                    sql.append("email = ?, ");
                    params.add(email);
                }
                if (!password.isEmpty()) {
                    sql.append("password = ?, ");
                    params.add(password);
                }

                // Remove the trailing comma and space
                sql.setLength(sql.length() - 2);
                sql.append(" WHERE id = ?");

                // Retrieve the user ID (replace this with actual implementation)
                int userId = getCurrentUserId();
                params.add(userId);

                PreparedStatement stmt = conn.prepareStatement(sql.toString());

                // Set the parameters dynamically
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                // Execute the update query
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Your account has been updated.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Update Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update your account. Please try again.");
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating your account.");
                alert.showAndWait();
            }
        });

        // Go Back Button
        Button goBackButton = new Button("Go back");
        applyButtonStyles(goBackButton);
        goBackButton.setPrefWidth(178);
        goBackButton.setAlignment(Pos.CENTER);

        // Action for goBackButton
        goBackButton.setOnAction(e -> {
            try {
                Main.instance.showMainMenu(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add UI elements to the VBox
        vbox.getChildren().addAll(label, emailLabel, emailField, passwordLabel, passwordField, saveButton, goBackButton);

        // Set Background Image
        Image image = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        vbox.setBackground(new Background(backgroundImage));

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
