package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.application.Platform;
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

/**
 * Test class for the {@link LoginForm} class, which handles user login functionality.
 * It tests scenarios such as successful login, failed login due to incorrect credentials,
 * and database connection errors.
 */
public class LoginFormTest {

    /**
     * Setup method to ensure the JavaFX application is initialized before any tests are run.
     * This method ensures that JavaFX components are available for the tests.
     */
    @BeforeAll
    public static void setupJavaFX() {
        // Make sure the JavaFX application thread is started
        if (!Platform.isFxApplicationThread()) {
            // Launch JavaFX application to initialize Toolkit
            Application.launch(JavaFXTestApp.class);
        }
    }

    /**
     * Simple JavaFX application class used to initialize the JavaFX toolkit for testing.
     * The application starts a basic stage to allow JavaFX elements to function.
     */
    public static class JavaFXTestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // Just initializing the stage to launch the JavaFX toolkit
            primaryStage.setTitle("JavaFX Test");
            primaryStage.show();
        }
    }

    /**
     * Test for the  method when login is successful.
     * Verifies that the main menu is shown after a successful login.
     */
    @Test
    public void testHandleLogin_SuccessfulLogin() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                Stage primaryStage = new Stage();
                LoginForm loginForm = new LoginForm();
                loginForm.show(primaryStage);

                // Locate the login fields and button by their IDs
                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set username and password for successful login
                usernameField.setText("testUser");
                passwordField.setText("testPassword");

                // Simulate the login button click
                loginButton.fire();

                // Perform assertions to ensure the behavior after login
                assertNotNull(primaryStage.getScene().lookup("#mainMenu"));

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread time to finish processing
        Thread.sleep(2000);
    }

    /**
     * Test for the  method when login fails due to incorrect credentials.
     * Verifies that an error alert is shown with the appropriate message.
     */
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

                // Locate the login fields and button
                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set wrong username and password
                usernameField.setText("wrongUser");
                passwordField.setText("wrongPassword");

                // Simulate login button click
                loginButton.fire();

                // Verify that the alert method was called with the correct parameters
                verify(loginFormSpy).showAlert(eq(Alert.AlertType.ERROR), eq("Login Failed"), eq("Invalid username or password."));
                verify(mockAlert).showAndWait(); // Check if the alert's showAndWait method was called

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread time to finish processing
        Thread.sleep(2000);
    }

    /**
     * Test for the  method when a database error occurs.
     * Verifies that an error alert is shown with a message indicating the database issue.
     */
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

                // Locate the login fields and button
                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");

                // Set username and password (simulate a database error)
                usernameField.setText("testUser");
                passwordField.setText("testPassword");

                // Simulate login button click
                loginButton.fire();

                // Verify that the alert method was called for database error
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