package com.jasmine.quizzy;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundEffect {

    public static void playSound(String soundFilePath) {
        try {
            URL resource = SoundEffect.class.getResource(soundFilePath);
            if (resource == null) {
                throw new RuntimeException("Sound file not found: " + soundFilePath);
            }
            String soundFileUrl = resource.toExternalForm();
            Media sound = new Media(soundFileUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

}
