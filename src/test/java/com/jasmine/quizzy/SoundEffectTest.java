package com.jasmine.quizzy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SoundEffectTest {

    @BeforeAll
    public static void setupJavaFX() {
        if (!Platform.isFxApplicationThread()) {
            Application.launch(JavaFXTestApp.class);
        }
    }

    public static class JavaFXTestApp extends Application {
        @Override
        public void start(javafx.stage.Stage primaryStage) {
            primaryStage.setTitle("JavaFX Test");
            primaryStage.show();
        }
    }

    @Test
    public void testPlaySoundWithValidFile() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Create a spy of the MediaPlayer class
                MediaPlayer mediaPlayerSpy = spy(MediaPlayer.class);

                // Mock the creation of the MediaPlayer to return the spy
                doReturn(mediaPlayerSpy).when(mediaPlayerSpy).play();

                // Call the method that plays the sound
                String soundFilePath = "/sounds/buttonPress.mp3"; // Example valid file path
                SoundEffect.playSound(soundFilePath);

                // Verify if the play() method was called once
                verify(mediaPlayerSpy, times(1)).play();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }

    @Test
    public void testPlaySoundWithInvalidFile() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                // Create a spy of the MediaPlayer class
                MediaPlayer mediaPlayerSpy = spy(MediaPlayer.class);

                // Mock the creation of the MediaPlayer to return the spy
                doReturn(mediaPlayerSpy).when(mediaPlayerSpy).play();

                // Simulate an invalid file path
                String invalidSoundFilePath = "/invalidPath.mp3"; // Invalid file path
                SoundEffect.playSound(invalidSoundFilePath);

                // Verify that the play() method was not called due to invalid file path
                verify(mediaPlayerSpy, times(0)).play();

            } catch (Exception e) {
                fail("Test failed due to exception: " + e.getMessage());
            }
        });

        // Sleep to allow JavaFX thread to process UI
        Thread.sleep(2000);
    }
}
