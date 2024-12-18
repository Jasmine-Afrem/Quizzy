package com.jasmine.quizzy;

import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BackgroundMusicTest {

    @Test
    public void testPlay() {
        // Mock the MediaPlayer
        MediaPlayer mockMediaPlayer = mock(MediaPlayer.class);

        // Create an instance of BackgroundMusic with the mocked MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic(mockMediaPlayer);

        // Call the play method
        backgroundMusic.play();

        // Verify that MediaPlayer's play method was called
        verify(mockMediaPlayer).play();
    }

    @Test
    public void testPause() {
        // Mock the MediaPlayer
        MediaPlayer mockMediaPlayer = mock(MediaPlayer.class);

        // Create an instance of BackgroundMusic with the mocked MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic(mockMediaPlayer);

        // Call the pause method
        backgroundMusic.pause();

        // Verify that MediaPlayer's pause method was called
        verify(mockMediaPlayer).pause();
    }

    @Test
    public void testStop() {
        // Mock the MediaPlayer
        MediaPlayer mockMediaPlayer = mock(MediaPlayer.class);

        // Create an instance of BackgroundMusic with the mocked MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic(mockMediaPlayer);

        // Call the stop method
        backgroundMusic.stop();

        // Verify that MediaPlayer's stop method was called
        verify(mockMediaPlayer).stop();
    }

    @Test
    public void testSetVolume() {
        // Mock the MediaPlayer
        MediaPlayer mockMediaPlayer = mock(MediaPlayer.class);

        // Create an instance of BackgroundMusic with the mocked MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic(mockMediaPlayer);

        // Set a specific volume
        double testVolume = 0.5;
        backgroundMusic.setVolume(testVolume);

        // Verify that MediaPlayer's setVolume method was called with the correct volume
        verify(mockMediaPlayer).setVolume(testVolume);
    }

    @Test
    public void testGetVolume() {
        // Mock the MediaPlayer
        MediaPlayer mockMediaPlayer = mock(MediaPlayer.class);

        // Stub the getVolume method to return a specific value
        double expectedVolume = 0.7;
        when(mockMediaPlayer.getVolume()).thenReturn(expectedVolume);

        // Create an instance of BackgroundMusic with the mocked MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic(mockMediaPlayer);

        // Get the volume
        double actualVolume = backgroundMusic.getVolume();

        // Assert that the returned volume is as expected
        assertEquals(expectedVolume, actualVolume);

        // Verify that MediaPlayer's getVolume method was called
        verify(mockMediaPlayer).getVolume();
    }

    @Test
    public void testNullMediaPlayer() {
        // Create an instance of BackgroundMusic with a null MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic((MediaPlayer) null);

        // Call methods and verify they handle the null case gracefully
        assertDoesNotThrow(() -> backgroundMusic.play());
        assertDoesNotThrow(() -> backgroundMusic.pause());
        assertDoesNotThrow(() -> backgroundMusic.stop());
        assertDoesNotThrow(() -> backgroundMusic.setVolume(0.5));
        assertEquals(0.0, backgroundMusic.getVolume());
    }
}
