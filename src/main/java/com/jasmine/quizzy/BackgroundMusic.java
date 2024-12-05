package com.jasmine.quizzy;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BackgroundMusic {
    private MediaPlayer mediaPlayer;

    public BackgroundMusic(String resourcePath) {
        try {
            // Load the media from the resources folder
            Media media = new Media(getClass().getResource(resourcePath).toString());
            mediaPlayer = new MediaPlayer(media);

            // Set the music to loop
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
        }
    }

    // Start or resume the music
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    // Pause the music
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Stop the music
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Set volume (0.0 to 1.0)
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    // Get current volume
    public double getVolume() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVolume();
        }
        return 0.0;
    }
}
