package com.jasmine.quizzy;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

            // Log the resource URL for debugging
            System.out.println("Loading sound from: " + resource.toExternalForm());

            // Create a Media object and MediaPlayer for the sound
            String soundFileUrl = resource.toExternalForm();
            Media sound = new Media(soundFileUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            // Play sound in a separate thread to prevent UI blocking
            new Thread(() -> {
                try {
                    mediaPlayer.play();  // Play the sound asynchronously
                    System.out.println("Sound played successfully.");
                } catch (Exception e) {
                    System.err.println("Error while playing sound: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
