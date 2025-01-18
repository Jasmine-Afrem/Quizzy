package com.jasmine.quizzy;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;

/**
 * The {@code ButtonStyler} class provides a set of static methods to apply custom styles to buttons,
 * including hover effects, click effects, and animations.
 */
public class ButtonStyler {

    /**
     * Applies custom styles to the given {@code Button}.
     * This method sets the font, background color, padding, and various effects for button states
     * (normal, hover, pressed, and released).
     * It also adds animations and sound effects for user interactions.
     *
     * @param button the {@code Button} to apply styles to
     */
    public static void applyButtonStyles(Button button) {
        // Set the initial font and style for the button
        button.setFont(Font.loadFont(ButtonStyler.class.getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18));
        button.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-radius: 25; "
                        + "-fx-padding: 10px 30px; "
                        + "-fx-background-color: linear-gradient(to bottom, #8e59ba, #5d2f8c); "
                        + "-fx-border-color: transparent; "
                        + "-fx-border-width: 2px; "
                        + "-fx-border-radius: 25; "
                        + "-fx-transition: all 0.3s ease-in-out;"
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");

        // Create a DropShadow with a violet glowing effect for the hover state
        DropShadow glowShadow = new DropShadow();
        glowShadow.setColor(Color.web("#8e59ba"));
        glowShadow.setRadius(15);
        glowShadow.setOffsetX(0);
        glowShadow.setOffsetY(0);

        // Hover effect: change background color, add glow effect, and animate scaling
        button.setOnMouseEntered(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25; "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #a56ceb, #6A4791); "
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.3s ease-in-out;"
            );
            button.setEffect(glowShadow);  // Apply the glow effect

            // Scale the button up during hover
            Timeline scaleTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleXProperty(), 1.1)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleYProperty(), 1.1))
            );
            scaleTimeline.setCycleCount(1);
            scaleTimeline.play();
        });

        // Mouse exit effect: reset background and remove glow
        button.setOnMouseExited(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25; "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #8e59ba, #5d2f8c); "
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.3s ease-in-out;"
            );
            button.setEffect(null);  // Remove the glow effect

            // Reset the scale animation
            Timeline scaleTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1.1)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleXProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleYProperty(), 1))
            );
            scaleTimeline.setCycleCount(1);
            scaleTimeline.play();
        });

        // Play a sound effect when the button is clicked
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> SoundEffect.playSound("/sounds/buttonPress.mp3"));

        // Pressed effect: change button style when clicked
        button.setOnMousePressed(event -> button.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-radius: 25; "
                        + "-fx-padding: 10px 30px; "
                        + "-fx-background-color: linear-gradient(to bottom, #8B008B, #6A4791); "
                        + "-fx-border-color: transparent; "
                        + "-fx-border-width: 2px; "
                        + "-fx-border-radius: 25; "
                        + "-fx-transition: all 0.2s ease-in-out;"
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);"));

        // Release effect: revert button style back to hover or normal
        button.setOnMouseReleased(event -> button.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-radius: 25; "
                        + "-fx-padding: 10px 30px; "
                        + "-fx-background-color: linear-gradient(to bottom, #9b56b7, #6A4791); "
                        + "-fx-border-color: transparent; "
                        + "-fx-border-width: 2px; "
                        + "-fx-border-radius: 25; "
                        + "-fx-transition: all 0.3s ease-in-out;"
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);"));
    }
}