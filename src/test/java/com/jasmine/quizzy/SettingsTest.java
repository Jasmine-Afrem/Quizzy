package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link Settings} class. This class contains tests for verifying
 * the functionality of saving settings, handling empty fields, and adjusting the volume.
 */
public class SettingsTest {

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
     * Test for saving settings with valid data. Verifies that the save action triggers
     * an alert confirming the settings were saved.
     */
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

    /**
     * Test for saving settings with empty fields. Verifies that the save action triggers
     * an alert notifying the user to fill in the required fields.
     */
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

    /**
     * Test for adjusting the volume using the volume slider. Verifies that the volume is
     * updated correctly when the slider value is changed.
     */
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
                assertEquals(0.8, Settings.getVolum(), "Volume should be adjusted to 0.8");

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }
}