package com.jasmine.quizzy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The {@code SignUpForm} class represents the user interface for the sign-up form in the application.
 * It allows users to input their email, username, and password to create a new account.
 * The class handles form submission, validation, password hashing, and database insertion.
 */
public class SignUpForm {

    /**
     * Displays the sign-up form and sets up the scene for user interaction.
     * It includes fields for email, username, password, and repeat password.
     * The form allows users to sign up or navigate back to the login screen.
     *
     * @param primaryStage The main stage (window) of the application, used to display the sign-up form.
     */
    public void show(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        Label emailLabel = new Label("Email");
        emailLabel.setFont(customFont);
        emailLabel.setTextFill(Color.WHITE);

        TextField emailField = new TextField();
        StyleUtil.styleTextField(emailField);
        emailField.setMaxWidth(300);

        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(customFont);
        usernameLabel.setTextFill(Color.WHITE);

        TextField usernameField = new TextField();
        StyleUtil.styleTextField(usernameField);
        usernameField.setMaxWidth(300);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(customFont);
        passwordLabel.setTextFill(Color.WHITE);

        PasswordField passwordField = new PasswordField();
        StyleUtil.styleTextField(passwordField);
        passwordField.setMaxWidth(300);

        Label repeatPasswordLabel = new Label("Repeat Password");
        repeatPasswordLabel.setFont(customFont);
        repeatPasswordLabel.setTextFill(Color.WHITE);

        PasswordField repeatPasswordField = new PasswordField();
        StyleUtil.styleTextField(repeatPasswordField);
        repeatPasswordField.setMaxWidth(300);

        Button signUpButton = new Button("Sign Up");
        ButtonStyler.applyButtonStyles(signUpButton);

        Button backButton = new Button("Back");
        ButtonStyler.applyButtonStyles(backButton);

        backButton.setOnAction(e -> new LoginForm().show(primaryStage));

        signUpButton.setOnAction(e -> handleSignUp(emailField.getText(), usernameField.getText(), passwordField.getText(), repeatPasswordField.getText(), primaryStage));

        signUpButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));

        backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));

        // Create the layout for the form fields
        VBox fieldsLayout = new VBox(15, emailLabel, emailField, usernameLabel, usernameField, passwordLabel, passwordField, repeatPasswordLabel, repeatPasswordField, signUpButton, backButton);
        fieldsLayout.setAlignment(Pos.CENTER);

        // Create a dark purple panel (only for the form)
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 500); // Adjusted size for the form
        darkPurplePanel.setMaxWidth(400); // Set maximum width
        darkPurplePanel.setMaxHeight(650);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");

        // Apply shadow effect to the panel
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));  // Darker gray shadow

        // Add the form layout to the dark purple panel
        darkPurplePanel.getChildren().add(fieldsLayout);

        // Set background image for the main layout
        StackPane root = new StackPane();

        // Background image
        root.setBackground(StyleUtil.createBackground("/background.jpg"));

        // Add the dark purple panel to the root
        root.getChildren().add(darkPurplePanel);

        // Scene setup
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setScene(scene);
    }

    /**
     * Handles the sign-up process by validating the user input, hashing the password,
     * and inserting the user's details into the database. If the sign-up is successful,
     * the user is redirected to the login form.
     *
     * @param email The email entered by the user.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param repeatPassword The password confirmation entered by the user.
     * @param primaryStage The main stage (window) of the application, used to navigate to the login form.
     */
    void handleSignUp(String email, String username, String password, String repeatPassword, Stage primaryStage) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "All fields must be filled.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Passwords do not match.");
            return;
        }

        // Hash the password before storing it in the database
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);  // Store the hashed password
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Your account has been created.");
            new LoginForm().show(primaryStage);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while creating your account.");
        }
    }

    /**
     * Hashes the user's password using the SHA-256 algorithm for secure storage.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();  // Return hashed password as a hexadecimal string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays a customized alert with the given type, title, and content.
     *
     * @param type The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert.
     * @param content The content message of the alert.
     */
    void showAlert(Alert.AlertType type, String title, String content) {
        // Create a new Alert of the specified type
        Alert alert = new Alert(type);

        // Set the title of the alert
        alert.setTitle(title);

        // Set the header of the alert (null as per original code)
        alert.setHeaderText(null);

        // Set the content of the alert
        alert.setContentText(content);

        // Load a custom icon from resources based on the alert type
        InputStream iconStream = switch (type) {
            case ERROR -> getClass().getResourceAsStream("/icons/error_icon.png");
            case INFORMATION -> getClass().getResourceAsStream("/icons/information_icon.png");
            default -> getClass().getResourceAsStream("/icons/default_icon.png");
        };

        if (iconStream != null) {
            // Set the custom icon
            Image iconImage = new Image(iconStream);
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(50);  // Set width for the icon
            iconView.setFitHeight(50); // Set height for the icon
            alert.setGraphic(iconView);
        } else {
            System.out.println("Icon not found!");
        }

        // Apply custom CSS styling to the alert
        alert.getDialogPane().getStyleClass().add("custom-alert");

        // Load custom font for the alert
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);

        // Define CSS for the alert's content area
        String css = "-fx-background-color: #4c226e; "
                + "-fx-font-family: '" + customFont.getName() + "'; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: white; "
                + "-fx-content-display: TOP;" // Set icon above the text
                + "-fx-alignment: CENTER;"   // Center text and icon
                + "-fx-padding: 20px;";      // Padding for more spacious feel
        alert.getDialogPane().setStyle(css);

        // Ensure the content text and label are white
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: white;");
        alert.getDialogPane().lookup(".label").setStyle("-fx-text-fill: white;");

        // Adjust the content area size to avoid truncation
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
}