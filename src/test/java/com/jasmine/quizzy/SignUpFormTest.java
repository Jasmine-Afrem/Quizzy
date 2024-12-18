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

public class SignUpFormTest {

    @BeforeAll
    public static void setupJavaFX() {
        if (!Platform.isFxApplicationThread()) {
            Application.launch(JavaFXTestApp.class);
        }
    }

    public static class JavaFXTestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("JavaFX Test");
            primaryStage.show();
        }
    }

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
