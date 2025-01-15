package com.jasmine.quizzy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpForm {

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

        signUpButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/sounds/buttonPress.mp3");
        });

        backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/sounds/buttonPress.mp3");
        });

        VBox layout = new VBox(15, emailLabel, emailField, usernameLabel, usernameField, passwordLabel, passwordField, repeatPasswordLabel, repeatPasswordField, signUpButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(StyleUtil.createBackground("/background.jpg"));

        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setScene(scene);
    }

    void handleSignUp(String email, String username, String password, String repeatPassword, Stage primaryStage) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "All fields must be filled.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Passwords do not match.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Your account has been created.");
            new LoginForm().show(primaryStage);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while creating your account.");
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
