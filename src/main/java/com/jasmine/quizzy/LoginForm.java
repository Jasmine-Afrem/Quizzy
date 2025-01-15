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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm {

    public void show(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Create and style UI elements
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
        passwordField.setMaxWidth(300);
        usernameField.setMaxWidth(300);

        // Apply shared styles
        StyleUtil.styleTextField(usernameField);
        StyleUtil.styleTextField(passwordField);

        // Create Login button
        Button loginButton = new Button("Login");
        loginButton.setFont(customFont);
        ButtonStyler.applyButtonStyles(loginButton);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            handleLogin(username, password, primaryStage);
        });

        loginButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/sounds/buttonPress.mp3");
        });

        // Create Sign-up text with hover effect
        Text signUpText = new Text("Don't have an account? Sign up");
        signUpText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Light.ttf"), 18));
        Font hoverFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);
        Font defaultFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Light.ttf"), 18);
        signUpText.setFill(Color.WHITE);
        signUpText.setUnderline(true);

        signUpText.setOnMouseEntered((MouseEvent event) -> {
            signUpText.setFont(hoverFont);
        });

        signUpText.setOnMouseExited((MouseEvent event) -> {
            signUpText.setFont(defaultFont);
        });

        signUpText.setOnMouseClicked(e -> new SignUpForm().show(primaryStage));

        // Logo with animation
        ImageView logoImageView = new ImageView(new Image(getClass().getResource("/Quizzy.png").toExternalForm()));
        logoImageView.setFitWidth(500);
        logoImageView.setPreserveRatio(true);
        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(15);
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.play();

        VBox fieldsLayout = new VBox(20, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signUpText);
        fieldsLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, logoImageView, fieldsLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setBackground(StyleUtil.createBackground("/background.jpg"));

        Scene loginScene = new Scene(mainLayout, 1024, 900);
        primaryStage.setScene(loginScene);
    }

    void handleLogin(String username, String password, Stage primaryStage) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // First, set the user data in the session
                SessionManager.setCurrentUser(rs.getInt("id"), rs.getString("username"));

                // Now proceed to the main menu with the username
                Main.userName = rs.getString("username");  // Set the username in Main
                new MainMenu(rs.getString("username")).show(primaryStage);  // Pass username to the main menu
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }


    void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
