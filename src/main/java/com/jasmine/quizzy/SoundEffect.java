package com.jasmine.quizzy;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class SoundEffect {

    // Method to play the sound asynchronously
    public static void playSound(String soundFilePath) {
        try {
            URL resource = SoundEffect.class.getResource(soundFilePath);

            if (resource == null) {
                System.err.println("Sound file not found: " + soundFilePath);
                return;  // Exit if the file is not found
            }

            // Create an AudioClip object and play it asynchronously
            AudioClip audioClip = new AudioClip(resource.toExternalForm());
            new Thread(() -> {
                try {
                    audioClip.play();  // Play sound asynchronously
                } catch (Exception e) {
                    System.err.println("Error while playing sound: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
