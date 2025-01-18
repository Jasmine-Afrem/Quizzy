package com.jasmine.quizzy;

import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link BackgroundMusic} class, which handles background music playback.
 * It tests methods like play, pause, stop, setVolume, getVolume, and handles cases for a null MediaPlayer.
 */
public class BackgroundMusicTest {

    /**
     * Test for the {@link BackgroundMusic#play()} method.
     * Verifies that the {@link MediaPlayer#play()} method is called when play() is invoked.
     */
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

    /**
     * Test for the {@link BackgroundMusic#pause()} method.
     * Verifies that the {@link MediaPlayer#pause()} method is called when pause() is invoked.
     */
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

    /**
     * Test for the {@link BackgroundMusic#stop()} method.
     * Verifies that the {@link MediaPlayer#stop()} method is called when stop() is invoked.
     */
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

    /**
     * Test for the {@link BackgroundMusic#setVolume(double)} method.
     * Verifies that the {@link MediaPlayer#setVolume(double)} method is called with the correct volume.
     */
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

    /**
     * Test for the {@link BackgroundMusic#getVolume()} method.
     * Verifies that the method returns the correct volume.
     */
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

    /**
     * Test for handling a null {@link MediaPlayer}.
     * Verifies that the methods do not throw exceptions when the MediaPlayer is null.
     */
    @Test
    public void testNullMediaPlayer() {
        // Create an instance of BackgroundMusic with a null MediaPlayer
        BackgroundMusic backgroundMusic = new BackgroundMusic((MediaPlayer) null);

        // Call methods and verify they handle the null case gracefully
        assertDoesNotThrow(backgroundMusic::play);
        assertDoesNotThrow(backgroundMusic::pause);
        assertDoesNotThrow(backgroundMusic::stop);
        assertDoesNotThrow(() -> backgroundMusic.setVolume(0.5));
        assertEquals(0.0, backgroundMusic.getVolume());
    }
}