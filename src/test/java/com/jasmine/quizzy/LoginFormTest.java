package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;

public class LoginFormTest {

    // Start JavaFX application before the tests
    @BeforeAll
    public static void setupJavaFX() {
        // Make sure the JavaFX application thread is started
        if (!Platform.isFxApplicationThread()) {
            // Launch JavaFX application to initialize Toolkit
            Application.launch(JavaFXTestApp.class);
        }
    }

    // Simple JavaFX application class to initialize the JavaFX toolkit
    public static class JavaFXTestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // Just initializing the stage to launch the JavaFX toolkit
            primaryStage.setTitle("JavaFX Test");
            primaryStage.show();
        }
    }

    @Test
    public void testHandleLogin_SuccessfulLogin() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                Stage primaryStage = new Stage();
                LoginForm loginForm = new LoginForm();
                loginForm.show(primaryStage);

                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set username and password for successful login
                usernameField.setText("testUser");
                passwordField.setText("testPassword");

                // Simulate the login button click
                loginButton.fire();

                // Perform assertions to ensure the behavior after login
                // For example, check that the main menu is shown
                assertTrue(primaryStage.getScene().lookup("#mainMenu") != null);

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread time to finish processing
        Thread.sleep(2000);
    }

    @Test
    public void testHandleLogin_FailedLogin() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Spy on the LoginForm to capture alert creation
                LoginForm loginFormSpy = Mockito.spy(new LoginForm());

                // Mock the alert method to simulate a failed login alert
                Alert mockAlert = mock(Alert.class);
                doReturn(mockAlert).when(loginFormSpy).showAlert(any(), anyString(), anyString());

                Stage primaryStage = new Stage();
                loginFormSpy.show(primaryStage);

                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set wrong username and password
                usernameField.setText("wrongUser");
                passwordField.setText("wrongPassword");

                // Simulate login button click
                loginButton.fire();

                // Verify if the showAlert method was called with the correct parameters
                verify(loginFormSpy).showAlert(eq(Alert.AlertType.ERROR), eq("Login Failed"), eq("Invalid username or password."));
                verify(mockAlert).showAndWait(); // Check if the alert's showAndWait method was called

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread time to finish processing
        Thread.sleep(2000);
    }

    @Test
    public void testHandleLogin_DatabaseError() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Spy on the LoginForm to capture alert creation
                LoginForm loginFormSpy = Mockito.spy(new LoginForm());

                // Mock the alert method to simulate a database error alert
                Alert mockAlert = mock(Alert.class);
                doReturn(mockAlert).when(loginFormSpy).showAlert(any(), anyString(), anyString());

                Stage primaryStage = new Stage();
                loginFormSpy.show(primaryStage);

                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set username and password (simulate a database error)
                usernameField.setText("testUser");
                passwordField.setText("testPassword");

                // Simulate login button click
                loginButton.fire();

                // Verify if the showAlert method was called for database error
                verify(loginFormSpy).showAlert(eq(Alert.AlertType.ERROR), eq("Database Error"), eq("An error occurred while connecting to the database."));
                verify(mockAlert).showAndWait(); // Check if the alert's showAndWait method was called

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread time to finish processing
        Thread.sleep(2000);
    }
}
