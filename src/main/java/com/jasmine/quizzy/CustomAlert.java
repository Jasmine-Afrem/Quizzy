package com.jasmine.quizzy;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.InputStream;

public class CustomAlert {

    public static void showAlert(String scoresInformation, String s, String message) {
        // Create a new Alert
        Alert alert = new Alert(AlertType.INFORMATION);

        // Set alert title
        alert.setTitle("Information");

        // Set the alert message
        alert.setHeaderText(null); // No header
        alert.setContentText(message);

        // Load custom icon image
        InputStream iconStream = CustomAlert.class.getResourceAsStream("/icons/information_icon.png");
        if (iconStream == null) {
            System.out.println("Icon not found!");
        } else {
            // If the icon is found, set it on the alert
            Image iconImage = new Image(iconStream);
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(50);  // Increase the size of the icon
            iconView.setFitHeight(50); // Increase the size of the icon
            alert.setGraphic(iconView);
        }

        // Apply custom CSS styling for alert
        alert.getDialogPane().getStyleClass().add("custom-alert"); // Apply custom style class

        // Load the custom font
        Font customFont = Font.loadFont(CustomAlert.class.getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);

        // Set the font and background color via CSS
        String css = "-fx-background-color: #4c226e; "
                + "-fx-font-family: '" + customFont.getName() + "'; "
                + "-fx-font-size: 18px; "
                + "-fx-text-fill: white;"   // Ensure text is white
                + "-fx-content-display: TOP;" // Set icon above the text (optional, you can remove this if not needed)
                + "-fx-alignment: CENTER;"   // Center text and icon
                + "-fx-padding: 20px;";   // Optional padding for a more spacious feel

        // Apply the styling to the dialog pane
        alert.getDialogPane().setStyle(css);

        // Ensure the content text is white
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: white;");

        // Ensure the label for the content text is also styled properly
        alert.getDialogPane().lookup(".label").setStyle("-fx-text-fill: white;");

        // Adjust the content area to prevent truncation
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // Ensures the content area has the preferred size

        // Handle button styling after the alert is shown
        alert.setOnShown(event -> {
            Button button = (Button) alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
            if (button != null) {
                // Apply rounded edges and initial button style
                button.setStyle("-fx-background-color: #6A4C9C; "
                        + "-fx-text-fill: white; "
                        + "-fx-border-radius: 15px; "
                        + "-fx-font-size: 16px; "
                        + "-fx-border-color: transparent;");  // Ensure the border is transparent

                // Add mouse hover event to lighten the button background and add a border
                button.setOnMouseEntered((MouseEvent me) -> {
                    button.setStyle("-fx-background-color: #7F55A0; "
                            + "-fx-text-fill: white; "
                            + "-fx-border-radius: 15px; "
                            + "-fx-font-size: 16px; "
                            + "-fx-border-color: white; "
                            + "-fx-border-width: 2px;");  // Add border on hover
                });

                // Revert back the style when mouse exits
                button.setOnMouseExited((MouseEvent me) -> {
                    button.setStyle("-fx-background-color: #6A4C9C; "
                            + "-fx-text-fill: white; "
                            + "-fx-border-radius: 15px; "
                            + "-fx-font-size: 16px; "
                            + "-fx-border-color: transparent;");  // No border when not hovering
                });
            }
        });

        // Show the alert
        alert.showAndWait();
    }
}