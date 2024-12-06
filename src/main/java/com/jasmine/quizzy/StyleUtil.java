package com.jasmine.quizzy;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;

public class StyleUtil {

    static void styleTextField(TextField textField) {
        textField.setStyle("-fx-font: 18 \"SourGummy-Medium\"; "
                + "-fx-text-fill: white;"  // Set text color to white
                + "-fx-background-color: #6a4791; "  // Set background color
                + "-fx-background-radius: 10; "
                + "-fx-padding: 12px; "
                + "-fx-border-color: #cccccc; "  // Border color
                + "-fx-border-width: 2px; "  // Set border thickness
                + "-fx-border-radius: 10; "
                + "-fx-focus-color: #4CAF50; "
                + "-fx-faint-focus-color: transparent;");
    }

    public static Background createBackground(String imagePath) {
        // Load the image
        Image backgroundImage = new Image(StyleUtil.class.getResource(imagePath).toExternalForm());

        // Set the background size to fully cover the area without repeating
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true); // Cover entire area

        // Set the image to cover the area, no repetition
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // Do not repeat the image
                BackgroundRepeat.NO_REPEAT, // Ensure no repetition
                BackgroundPosition.CENTER,  // Center the image
                backgroundSize
        );

        // Return the Background with the correct settings
        return new Background(bgImage);
    }

}

