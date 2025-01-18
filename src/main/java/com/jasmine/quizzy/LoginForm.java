package com.jasmine.quizzy;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The {@code LoginForm} class represents the login page of the application.
 * It includes functionality for users to enter their credentials, authenticate, and navigate to the main menu.
 */
public class LoginForm {

    /**
     * Displays the login form to the user.
     * The page includes input fields for username and password, a "Login" button, and a "Sign-up" link for new users.
     * The form also features a floating logo and a stylized dark purple panel for the login fields.
     *
     * @param primaryStage The primary stage (window) of the application.
     */
    public void show(Stage primaryStage) {
        // Load the custom font for the page
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Create and style UI elements for username and password
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(customFont);
        usernameLabel.setTextFill(Color.WHITE);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(customFont);
        passwordLabel.setTextFill(Color.WHITE);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPrefSize(300, 40);
        passwordField.setPrefSize(300, 40);
        usernameField.setMaxWidth(300);
        passwordField.setMaxWidth(300);
        usernameField.setAlignment(Pos.CENTER);
        passwordField.setAlignment(Pos.CENTER);

        // Apply shared styles to text fields
        StyleUtil.styleTextField(usernameField);
        StyleUtil.styleTextField(passwordField);

        // Create and style the login button
        Button loginButton = new Button("Login");
        loginButton.setFont(customFont);
        ButtonStyler.applyButtonStyles(loginButton);

        // Set login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            handleLogin(username, password, primaryStage);
        });

        // Add sound effect to the login button
        loginButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));

        // Create sign-up text with hover effect
        Text signUpText = new Text("Don't have an account? Sign up");
        signUpText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Light.ttf"), 18));
        signUpText.setFill(Color.WHITE);
        signUpText.setUnderline(true);

        // Apply hover effect to the sign-up text
        Font hoverFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);
        Font defaultFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Light.ttf"), 18);
        signUpText.setOnMouseEntered(event -> signUpText.setFont(hoverFont));
        signUpText.setOnMouseExited(event -> signUpText.setFont(defaultFont));

        // Navigate to the sign-up form on click
        signUpText.setOnMouseClicked(e -> new SignUpForm().show(primaryStage));

        // Create and animate the logo
        ImageView logoImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/Quizzy.png")).toExternalForm()));
        logoImageView.setFitWidth(500);
        logoImageView.setPreserveRatio(true);

        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(-20);
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.play();

        // Create the layout for the form
        VBox fieldsLayout = new VBox(20, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signUpText);
        fieldsLayout.setAlignment(Pos.CENTER);

        // Create a styled panel for the form
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 400);
        darkPurplePanel.setMaxWidth(400);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));
        darkPurplePanel.getChildren().add(fieldsLayout);

        // Set up the main layout with background and logo
        StackPane root = new StackPane();
        root.setBackground(StyleUtil.createBackground("/background.jpg"));
        VBox mainLayout = new VBox(20, logoImageView, darkPurplePanel);
        mainLayout.setAlignment(Pos.CENTER);
        root.getChildren().add(mainLayout);

        // Set up the scene and display it
        Scene loginScene = new Scene(root, 1024, 900);
        primaryStage.setScene(loginScene);
    }

    /**
     * Handles user login by validating the entered username and password against the database.
     * If the login is successful, the user is navigated to the main menu.
     *
     * @param username     The entered username.
     * @param password     The entered password.
     * @param primaryStage The primary stage of the application.
     */
    void handleLogin(String username, String password, Stage primaryStage) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
            String hashedPassword = hashPassword(password);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                SessionManager.setCurrentUser(rs.getInt("id"), rs.getString("username"));
                Main.userName = rs.getString("username");
                new MainMenu(rs.getString("username")).show(primaryStage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    /**
     * Hashes the given password using SHA-256.
     *
     * @param password The password to hash.
     * @return The hashed password as a hexadecimal string, or {@code null} if an error occurs.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, and content message.
     *
     * @param type    The type of the alert (e.g., ERROR, INFORMATION).
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}