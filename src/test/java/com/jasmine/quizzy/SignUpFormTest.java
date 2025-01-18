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
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link SignUpForm} class. This class contains tests for verifying
 * the functionality of the sign-up form, including handling valid and invalid data inputs.
 */
public class SignUpFormTest {

    /**
     * Setup method to ensure the JavaFX application is initialized before any tests are run.
     * This method ensures that JavaFX components are available for the tests.
     */
    @BeforeAll
    public static void setupJavaFX() {
        // Ensure the JavaFX application thread is started if not already initialized
        if (!Platform.isFxApplicationThread()) {
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
            primaryStage.setTitle("JavaFX Test");
            primaryStage.show();
        }
    }

    /**
     * Test for signing up with valid data. Verifies that when the user enters valid email, username,
     * and matching passwords, a success alert is triggered.
     */
    @Test
    public void testSignUpWithValidData() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Spy on the SignUpForm to mock alert creation
                SignUpForm signUpFormSpy = spy(new SignUpForm());

                // Mock alert creation
                Alert mockAlert = mock(Alert.class);
                doReturn(mockAlert).when(signUpFormSpy).showAlert(any(), anyString(), anyString());

                Stage primaryStage = new Stage();
                signUpFormSpy.show(primaryStage);

                // Lookup UI elements
                TextField emailField = (TextField) primaryStage.getScene().lookup("#emailField");
                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                PasswordField repeatPasswordField = (PasswordField) primaryStage.getScene().lookup("#confirmPasswordField");
                Button signUpButton = (Button) primaryStage.getScene().lookup("#signUpButton");

                // Set valid input data
                emailField.setText("test@example.com");
                usernameField.setText("testUser");
                passwordField.setText("password123");
                repeatPasswordField.setText("password123");

                // Simulate button click
                signUpButton.fire();

                // Verify success alert
                verify(signUpFormSpy).showAlert(eq(Alert.AlertType.INFORMATION), eq("Sign Up Successful"), eq("Your account has been created."));
                verify(mockAlert).showAndWait();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }

    /**
     * Test for signing up with empty fields. Verifies that when required fields are left empty,
     * an error alert is triggered indicating that all fields must be filled.
     */
    @Test
    public void testSignUpWithEmptyFields() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Spy on the SignUpForm to mock alert creation
                SignUpForm signUpFormSpy = spy(new SignUpForm());

                // Mock alert creation
                Alert mockAlert = mock(Alert.class);
                doReturn(mockAlert).when(signUpFormSpy).showAlert(any(), anyString(), anyString());

                Stage primaryStage = new Stage();
                signUpFormSpy.show(primaryStage);

                // Lookup UI elements
                TextField emailField = (TextField) primaryStage.getScene().lookup("#emailField");
                Button signUpButton = (Button) primaryStage.getScene().lookup("#signUpButton");

                // Leave fields empty
                emailField.setText("");

                // Simulate button click
                signUpButton.fire();

                // Verify error alert
                verify(signUpFormSpy).showAlert(eq(Alert.AlertType.ERROR), eq("Sign Up Failed"), eq("All fields must be filled."));
                verify(mockAlert).showAndWait();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }

    /**
     * Test for signing up with mismatched passwords. Verifies that when the password and confirmation
     * password do not match, an error alert is triggered indicating the mismatch.
     */
    @Test
    public void testSignUpWithPasswordMismatch() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Spy on the SignUpForm to mock alert creation
                SignUpForm signUpFormSpy = spy(new SignUpForm());

                // Mock alert creation
                Alert mockAlert = mock(Alert.class);
                doReturn(mockAlert).when(signUpFormSpy).showAlert(any(), anyString(), anyString());

                Stage primaryStage = new Stage();
                signUpFormSpy.show(primaryStage);

                // Lookup UI elements
                TextField emailField = (TextField) primaryStage.getScene().lookup("#emailField");
                TextField usernameField = (TextField) primaryStage.getScene().lookup("#usernameField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                PasswordField repeatPasswordField = (PasswordField) primaryStage.getScene().lookup("#confirmPasswordField");
                Button signUpButton = (Button) primaryStage.getScene().lookup("#signUpButton");

                // Set mismatched passwords
                emailField.setText("test@example.com");
                usernameField.setText("testUser");
                passwordField.setText("password123");
                repeatPasswordField.setText("password456");

                // Simulate button click
                signUpButton.fire();

                // Verify error alert
                verify(signUpFormSpy).showAlert(eq(Alert.AlertType.ERROR), eq("Sign Up Failed"), eq("Passwords do not match."));
                verify(mockAlert).showAndWait();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }
}