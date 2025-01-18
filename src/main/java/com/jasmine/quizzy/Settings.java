package com.jasmine.quizzy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.jasmine.quizzy.SessionManager.getCurrentUserId;

/**
 * The Settings class provides the user interface and logic for the settings page of the application.
 * This includes the ability to update the user's email and password, adjust the volume, and log out.
 */
public class Settings {

    private static double volum = 0.5; // Default volume value

    /**
     * Gets the current volume level.
     *
     * @return the current volume level
     */
    public static double getVolum() {
        return volum;
    }

    /**
     * Sets a new volume level.
     *
     * @param v the new volume level to set
     */
    public static void setVolum(double v) {
        volum = v;
    }

    /**
     * Initializes the settings page UI and logic for actions like updating user info, adjusting volume,
     * logging out, and navigating to the main menu.
     *
     * @param primaryStage the primary stage to display the scene
     */
    public void start(Stage primaryStage) {
        // Load custom fonts
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);
        Font fontText = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Title Label
        Label settingsLabel = new Label("Settings");
        settingsLabel.setFont(customFont);
        settingsLabel.setTextFill(Color.web("#ffffff"));
        settingsLabel.setMouseTransparent(true); // Prevents blocking clicks

        // User info fields
        Label userEmailLabel = new Label("Email: ");
        userEmailLabel.setFont(fontText);
        userEmailLabel.setTextFill(Color.web("#ffffff"));

        TextField userEmailField = new TextField();
        userEmailField.setPrefSize(300, 40); // Match the example styling
        userEmailField.setMaxWidth(300);
        StyleUtil.styleTextField(userEmailField);  // Apply styling from StyleUtil
        userEmailField.setPromptText("Enter new email");

        Label passwordLabel = new Label("Password: ");
        passwordLabel.setFont(fontText);
        passwordLabel.setTextFill(Color.web("#ffffff"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300, 40); // Match the example styling
        passwordField.setMaxWidth(300);
        StyleUtil.styleTextField(passwordField);  // Apply styling from StyleUtil
        passwordField.setPromptText("Enter new password");

        // Create and style buttons
        Button saveButton = new Button("Save Changes");
        Button logoutButton = new Button("Log Out");
        Button backButton = new Button("Go Back");
        ButtonStyler.applyButtonStyles(saveButton);
        ButtonStyler.applyButtonStyles(logoutButton);
        ButtonStyler.applyButtonStyles(backButton);
        saveButton.setPrefSize(200, 50);
        logoutButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        // Add sound effects to buttons
        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));
        backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));
        logoutButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));

        // Save button action: updates email and password in the database
        saveButton.setOnAction(e -> updateUserSettings(userEmailField, passwordField));

        // Back button action: navigates to the main menu
        backButton.setAlignment(Pos.CENTER);
        backButton.setOnAction(e -> navigateBackToMainMenu(primaryStage));

        // Logout button action: logs out the user after confirmation
        logoutButton.setOnAction(event -> logoutUser(primaryStage));

        // Volume adjustment section
        Label volumeLabel = new Label("Adjust Volume:");
        volumeLabel.setTextFill(Color.web("#ffffff"));
        volumeLabel.setFont(fontText);

        Slider volumeSlider = new Slider(0, 1, volum);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue();
            setVolum(volume); // Update static volume variable
            Main.backgroundMusic.setVolume(volume); // Adjust the background music volume
        });

        // Styling for volume slider
        volumeSlider.setMaxWidth(300);
        volumeSlider.setStyle("-fx-font: 18 \"SourGummy-Medium\"; "
                + "-fx-text-fill: white; "
                + "-fx-background-color: #6A4791; "
                + "-fx-background-radius: 10; "
                + "-fx-padding: 12px; "
                + "-fx-border-color: #cccccc; "
                + "-fx-border-width: 2px; "
                + "-fx-border-radius: 10; ");

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

        // Create a dark purple panel
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 500);
        darkPurplePanel.setMaxWidth(400);
        darkPurplePanel.setMaxHeight(650);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new DropShadow(10, Color.DARKGRAY));

        // Add the layout to the panel
        darkPurplePanel.getChildren().add(layout);

        // Set background for the root layout
        StackPane root = new StackPane();
        root.setBackground(StyleUtil.createBackground("/background.jpg"));
        root.getChildren().add(darkPurplePanel);

        // Scene setup
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Updates the user's email and/or password in the database.
     * Displays a custom alert if the update is successful or fails.
     *
     * @param userEmailField the text field where the user enters the new email
     * @param passwordField  the password field where the user enters the new password
     */
    private void updateUserSettings(TextField userEmailField, PasswordField passwordField) {
        String email = userEmailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() && password.isEmpty()) {
            CustomAlert.showAlert("Please fill in at least one field (email or password).");
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
                CustomAlert.showAlert("Your account has been updated.");
            } else {
                CustomAlert.showAlert("Failed to update your account. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert("An error occurred while updating your account.");
        }
    }

    /**
     * Navigates the user back to the main menu.
     *
     * @param primaryStage the primary stage to display the scene
     */
    private void navigateBackToMainMenu(Stage primaryStage) {
        try {
            new MainMenu("User").show(primaryStage);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Logs out the current user by displaying a custom confirmation alert asking for confirmation
     * before proceeding. If the user confirms, it clears the session and redirects them to the login screen.
     *
     * @param primaryStage The primary stage (window) of the application, used to show the login form after logout.
     */
    private void logoutUser(Stage primaryStage) {
        // Show the custom confirmation alert
        CustomAlert.showAlert("Are you sure you want to log out?");

        // Assuming the custom alert only closes when user clicks OK, you may handle this event here
        CustomAlert.showAlert("You have been logged out!");

        // Clear session and redirect to login
        SessionManager.logout();
        try {
            // Show the login form after logout
            new LoginForm().show(primaryStage);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}