package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SettingsTest {

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
    public void testSaveSettingsWithValidData() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                Settings settings = new Settings(); // Direct instance, no spying needed

                Stage primaryStage = new Stage();
                settings.start(primaryStage);

                // Lookup UI elements
                TextField userEmailField = (TextField) primaryStage.getScene().lookup("#userEmailField");
                PasswordField passwordField = (PasswordField) primaryStage.getScene().lookup("#passwordField");
                Button saveButton = (Button) primaryStage.getScene().lookup("#saveButton");

                // Set valid input data
                userEmailField.setText("newemail@example.com");
                passwordField.setText("newPassword123");

                // Spy the alert creation and capture it
                Alert alertSpy = spy(Alert.class);
                doNothing().when(alertSpy).showAndWait();

                // Simulate button click and verify alert is triggered
                saveButton.fire();

                // Verify if alert was triggered
                verify(alertSpy, times(1)).showAndWait();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }

    @Test
    public void testSaveSettingsWithEmptyFields() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                Settings settings = new Settings(); // Direct instance, no spying needed

                Stage primaryStage = new Stage();
                settings.start(primaryStage);

                // Lookup UI elements
                TextField userEmailField = (TextField) primaryStage.getScene().lookup("#userEmailField");
                Button saveButton = (Button) primaryStage.getScene().lookup("#saveButton");

                // Leave fields empty
                userEmailField.setText("");

                // Spy the alert creation and capture it
                Alert alertSpy = spy(Alert.class);
                doNothing().when(alertSpy).showAndWait();

                // Simulate button click and verify alert is triggered
                saveButton.fire();

                // Verify if alert was triggered
                verify(alertSpy, times(1)).showAndWait();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }

    @Test
    public void testAdjustVolume() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                Settings settings = new Settings(); // Direct instance, no spying needed

                Stage primaryStage = new Stage();
                settings.start(primaryStage);

                // Lookup volume slider
                Slider volumeSlider = (Slider) primaryStage.getScene().lookup("#volumeSlider");

                // Set volume slider value to 0.8
                volumeSlider.setValue(0.8);

                // Verify the volume change
                assertEquals(0.8, settings.getVolum(), "Volume should be adjusted to 0.8");

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }
}
