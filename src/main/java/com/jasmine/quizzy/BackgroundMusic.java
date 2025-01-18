package com.jasmine.quizzy;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * The {@code BackgroundMusic} class is responsible for controlling the background music in the application.
 * It allows for playing, pausing, stopping, and adjusting the volume of the background music.
 */
public class BackgroundMusic {
    private MediaPlayer mediaPlayer;

    /**
     * Constructs a {@code BackgroundMusic} object for production use with a specified resource path for the music.
     *
     * @param resourcePath the path to the music resource
     */
    public BackgroundMusic(String resourcePath) {
        try {
            // Load the background music from the given resource path
            Media media = new Media(Objects.requireNonNull(getClass().getResource("/sounds/backgroundMusic.mp3")).toString());
            this.mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Music plays indefinitely
        } catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
        }
    }

    /**
     * Constructs a {@code BackgroundMusic} object for testing with an existing {@code MediaPlayer}.
     *
     * @param mediaPlayer the {@code MediaPlayer} to be used for playing music
     */
    public BackgroundMusic(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Plays the background music if it is available.
     */
    public void play() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    /**
     * Pauses the background music if it is currently playing.
     */
    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    /**
     * Stops the background music and resets it to the beginning.
     */
    public void stop() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    /**
     * Sets the volume of the background music.
     *
     * @param volume a double value representing the volume (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) mediaPlayer.setVolume(volume);
    }

    /**
     * Retrieves the current volume of the background music.
     *
     * @return the current volume of the music (0.0 to 1.0)
     */
    public double getVolume() {
        return mediaPlayer != null ? mediaPlayer.getVolume() : 0.0;
    }
}