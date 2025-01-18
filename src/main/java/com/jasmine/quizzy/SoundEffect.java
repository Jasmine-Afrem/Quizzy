package com.jasmine.quizzy;

import javafx.scene.media.AudioClip;
import java.net.URL;

/**
 * The {@code SoundEffect} class provides a utility to play sound effects in the application.
 * It loads sound files and plays them asynchronously to avoid blocking the main thread.
 */
public class SoundEffect {

    /**
     * Plays the sound asynchronously using the specified sound file path.
     * The method first checks if the sound file exists and then plays it asynchronously on a separate thread.
     *
     * @param soundFilePath The path to the sound file to be played (relative to the classpath).
     *                      Example: "/sounds/buttonPress.mp3".
     */
    public static void playSound(String soundFilePath) {
        try {
            // Get the URL resource of the sound file from the classpath
            URL resource = SoundEffect.class.getResource(soundFilePath);

            // Check if the sound file exists
            if (resource == null) {
                System.err.println("Sound file not found: " + soundFilePath);
                return;  // Exit if the file is not found
            }

            // Create an AudioClip object from the sound file URL
            AudioClip audioClip = new AudioClip(resource.toExternalForm());

            // Play the sound asynchronously in a new thread
            new Thread(() -> {
                try {
                    audioClip.play();  // Play the sound clip asynchronously
                } catch (Exception e) {
                    System.err.println("Error while playing sound: " + e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            // Catch any general exceptions and print error message
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}