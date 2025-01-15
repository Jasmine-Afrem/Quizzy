package com.jasmine.quizzy;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ButtonStyler {

    public static void applyButtonStyles(Button button) {
        button.setFont(Font.loadFont(ButtonStyler.class.getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18));
        button.setStyle(
                "-fx-font: 18 \"SourGummy-Medium\"; "  // Use the custom font
                        + "-fx-text-fill: white; "
                        + "-fx-background-radius: 25; "
                        + "-fx-padding: 10px 30px; "
                        + "-fx-background-color: linear-gradient(to bottom, #7752a1, #4B0082); "  // Purple to indigo gradient
                        + "-fx-border-color: transparent; "
                        + "-fx-border-width: 2px; "
                        + "-fx-border-radius: 25; "
                        + "-fx-transition: all 0.3s ease-in-out;"  // Smooth transition for background and scaling
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");  // Shadow effect

        // Hover effect
        button.setOnMouseEntered(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "  // Use the custom font
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25; "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #a56ceb, #6A4791); "  // Pink to purple gradient on hover
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.3s ease-in-out;"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // Play sound on click
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/sounds/buttonPress.mp3");
        });

        // Mouse exit (reset the background)
        button.setOnMouseExited(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "  // Use the custom font
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25; "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #7752a1, #4B0082); "  // Back to original gradient
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.3s ease-in-out;"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // Pressed effect
        button.setOnMousePressed(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "  // Use the custom font
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25; "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #8B008B, #6A4791); "  // Darker pink to purple
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.2s ease-in-out;"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });

        // When mouse is released, reset back to hover state or normal
        button.setOnMouseReleased(event -> {
            button.setStyle(
                    "-fx-font: 18 \"SourGummy-Medium\"; "  // Use the custom font
                            + "-fx-text-fill: white; "
                            + "-fx-background-radius: 25;   "
                            + "-fx-padding: 10px 30px; "
                            + "-fx-background-color: linear-gradient(to bottom, #FF1493, #6A4791); "  // Reapply hover gradient after click
                            + "-fx-border-color: transparent; "
                            + "-fx-border-width: 2px; "
                            + "-fx-border-radius: 25; "
                            + "-fx-transition: all 0.3s ease-in-out;"
                            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 8, 0, 0, 2);");
        });
}}
