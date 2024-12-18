package com.jasmine.quizzy;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneTransition {

    public void fadeTransition(Stage primaryStage, Scene newScene) {
        // Fade out the current scene
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            // Change scene after fade-out is complete
            primaryStage.setScene(newScene);
            primaryStage.show();

            // Fade in the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), newScene.getRoot());
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
