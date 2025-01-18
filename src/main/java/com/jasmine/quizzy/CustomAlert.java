package com.jasmine.quizzy;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * The {@code CustomAlert} class is used to create and display customized alert dialogs.
 * This includes applying custom icons, fonts, animations, and styling to the alert.
 * It is primarily used to show informational messages to the user with a unique visual appearance.
 */
public class CustomAlert {

    /**
     * Displays a customized alert with the specified message and styling.
     * The alert includes a custom icon, font, background color, and a button with animation effects.
     *
     * @param message The main message to be displayed in the alert.
     */
    public static void showAlert(String message) {
        // Create a new Alert of type INFORMATION
        Alert alert = new Alert(AlertType.INFORMATION);

        // Set the title of the alert
        alert.setTitle("Information");

        // Set the content of the alert
        alert.setHeaderText(null); // No header
        alert.setContentText(message);

        // Load custom icon image from resources
        InputStream iconStream = CustomAlert.class.getResourceAsStream("/icons/information_icon.png");
        if (iconStream == null) {
            System.out.println("Icon not found!");
        } else {
            // If the icon is found, set it on the alert
            Image iconImage = new Image(iconStream);
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(50);  // Set width for the icon
            iconView.setFitHeight(50); // Set height for the icon
            alert.setGraphic(iconView);
        }

        // Apply custom CSS styling to the alert
        alert.getDialogPane().getStyleClass().add("custom-alert");

        // Load and apply custom font for the alert
        Font customFont = Font.loadFont(CustomAlert.class.getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);

        // Define the CSS styling for the alert dialog
        String css = "-fx-background-color: #4c226e; "
                + "-fx-font-family: '" + customFont.getName() + "'; "
                + "-fx-font-size: 16px; " // Set font size
                + "-fx-text-fill: white;"   // Set text color to white
                + "-fx-content-display: TOP;" // Set icon above the text
                + "-fx-alignment: CENTER;"   // Center text and icon
                + "-fx-padding: 20px;";      // Padding for more spacious feel

        // Apply the CSS to the dialog pane
        alert.getDialogPane().setStyle(css);

        // Ensure that the content text and label are white
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: white;");
        alert.getDialogPane().lookup(".label").setStyle("-fx-text-fill: white;");

        // Adjust the content area size to avoid truncation
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        // Apply button styling and animation when the alert is shown
        alert.setOnShown(event -> {
            Button button = (Button) alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
            if (button != null) {
                // Apply modern button styles
                ButtonStyler.applyButtonStyles(button);

                // Apply scaling animation to the button
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 0)),
                        new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleXProperty(), 1)),
                        new KeyFrame(Duration.seconds(0.3), new KeyValue(button.scaleYProperty(), 1))
                );
                timeline.setCycleCount(1);
                timeline.play();
            }

            // Apply fade-in animation for the alert
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(alert.getDialogPane().opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(alert.getDialogPane().opacityProperty(), 1))
            );
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.play();
        });

        // Apply fade-out animation when the OK button is clicked
        alert.setOnHidden(event -> {
            Button okButton = (Button) alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
            if (okButton != null) {
                okButton.setOnAction(actionEvent -> {
                    // Fade-out animation for alert
                    Timeline fadeOutTimeline = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(alert.getDialogPane().opacityProperty(), 1)),
                            new KeyFrame(Duration.seconds(0.3), new KeyValue(alert.getDialogPane().opacityProperty(), 0))
                    );
                    fadeOutTimeline.setOnFinished(finishEvent -> {
                        alert.close(); // Close the alert after fade-out animation
                    });
                    fadeOutTimeline.play();
                });
            }
        });

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
}