package com.jasmine.quizzy;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FirstPage {

    public void show(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        Image logoImage = new Image(getClass().getResource("/Quizzy.png").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(500);
        logoImageView.setPreserveRatio(true);

        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(15);
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.play();

        Button playButton = new Button("Play");
        ButtonStyler.applyButtonStyles(playButton); // Refactor button styling into a utility class

        playButton.setOnAction(event -> {
            new LoginForm().show(primaryStage); // Navigate to the login form
        });

        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        VBox mainLayout = new VBox(30);
        mainLayout.getChildren().addAll(logoImageView, playButton);
        mainLayout.setAlignment(Pos.CENTER);

        Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        mainLayout.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));

        Scene scene = new Scene(mainLayout, 1024, 900);
        primaryStage.setScene(scene);
        primaryStage.show(); // This makes the stage visible
    }
}
