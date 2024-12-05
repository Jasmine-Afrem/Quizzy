package com.jasmine.quizzy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


public class Main extends Application {

    public static Main instance;
    public static String userName;
    public static BackgroundMusic backgroundMusic;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primaryStage.setTitle("Quizzy");
        backgroundMusic = new BackgroundMusic("/backgroundMusic.mp3");
        backgroundMusic.setVolume(0.3);
        backgroundMusic.play();
        showLoginForm(primaryStage); // Call the loginForm method to set up the login form
    }

    public void showLoginForm(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);

        // Create labels for "Username" and "Password"
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(customFont);
        usernameLabel.setTextFill(Color.web("#ffffff"));  // White text color for labels

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(customFont);
        passwordLabel.setTextFill(Color.web("#ffffff"));  // White text color for labels

        // Create fields for user input with adjusted sizes
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Set a fixed width for the text fields
        usernameField.setPrefWidth(300);
        passwordField.setPrefWidth(300);

        // Set height for the input fields
        usernameField.setPrefHeight(40);
        passwordField.setPrefHeight(40);

        // Optionally, limit maximum width
        usernameField.setMaxWidth(300);
        passwordField.setMaxWidth(300);

        // Apply styling to input fields
        styleTextField(usernameField);
        styleTextField(passwordField);

        // Create a logo ImageView and adjust size
        Image logoImage = new Image(getClass().getResource("/Quizzy.png").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(500);
        logoImageView.setPreserveRatio(true);

        // Add the floating animation for the logo
        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(15);
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.play();

        // Add some spacing between the logo and the form
        VBox logoSpacingLayout = new VBox(20);
        logoSpacingLayout.setAlignment(Pos.CENTER);
        logoSpacingLayout.getChildren().add(logoImageView);

        // Create login button with increased size and styling
        Button loginButton = new Button("Login");
        loginButton.setFont(customFont);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Connect to the database and check credentials
            // Class-level variable to hold the logged-in user's ID
            int currentUserId = -1;

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Login successful, store user ID
                    SessionManager.setCurrentUserId(rs.getInt("id")); // Assuming 'id' is the column name for the user ID in the 'users' table
                    userName = username;
                    showMainMenu(primaryStage);
                } else {
                    // Login failed, show an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password.");
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });

        // Apply styles to login button
        applyButtonStyles(loginButton);

        Font fontSignUp = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Light.ttf"), 18);
        Font fontSignUpBold = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18);

        // Sign-up link text
        Text signUpText = new Text("Don't have an account? Sign up");
        signUpText.setFont(fontSignUp);
        signUpText.setFill(Color.WHITE);
        signUpText.setUnderline(true);

        VBox signUpTextSpace = new VBox(10);
        logoSpacingLayout.getChildren().add(signUpText);

        // Animate font transition for hover effects
        signUpText.setOnMouseEntered(event -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(200), e -> signUpText.setFont(fontSignUpBold))
            );
            timeline.play();
        });

        signUpText.setOnMouseExited(event -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(200), e -> signUpText.setFont(fontSignUp))
            );
            timeline.play();
        });

        // Click action to switch to sign-up form
        signUpText.setOnMouseClicked(event -> showSignUpForm(primaryStage, customFont));

        // Create layout for username and password fields
        VBox fieldsLayout = new VBox(10);
        fieldsLayout.setAlignment(Pos.CENTER);
        fieldsLayout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField);

        // Add some extra space between the form and the login button
        Region spacingRegion = new Region();
        spacingRegion.setPrefHeight(15);
        fieldsLayout.getChildren().add(spacingRegion);

        // Add the login button and sign-up text below the fields
        fieldsLayout.getChildren().addAll(loginButton, signUpText);

        // Create a main layout combining the logo and form
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(logoSpacingLayout, fieldsLayout);

        // Set a background image for the layout
        Image image = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        loginLayout.setBackground(new Background(backgroundImage));

        loginButton.setFont(customFont);

        Scene loginScene = new Scene(loginLayout, 1024, 900);
        primaryStage.setScene(loginScene);

        primaryStage.setMaxWidth(1024);
        primaryStage.setMaxHeight(900);

        primaryStage.show();
    }

    private void showSignUpForm(Stage primaryStage, Font customFont) {
        Label emailLabel = new Label("Email");
        emailLabel.setFont(customFont);
        emailLabel.setTextFill(Color.web("#ffffff"));

        TextField emailField = new TextField();
        styleTextField(emailField);

        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(customFont);
        usernameLabel.setTextFill(Color.web("#ffffff"));

        TextField usernameField = new TextField();
        styleTextField(usernameField);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(customFont);
        passwordLabel.setTextFill(Color.web("#ffffff"));

        PasswordField passwordField = new PasswordField();
        styleTextField(passwordField);

        Label repeatPasswordLabel = new Label("Repeat Password");
        repeatPasswordLabel.setFont(customFont);
        repeatPasswordLabel.setTextFill(Color.web("#ffffff"));

        PasswordField repeatPasswordField = new PasswordField();
        styleTextField(repeatPasswordField);

        // Set a fixed width for the text fields (to avoid full-width expansion)
        usernameField.setPrefWidth(300);  // Set preferred width
        passwordField.setPrefWidth(300);  // Set preferred width

        // Optionally, limit maximum width
        usernameField.setMaxWidth(300);   // Set maximum width
        passwordField.setMaxWidth(300);   // Set maximum width
        emailField.setMaxWidth(300);
        emailField.setMaxHeight(300);
        repeatPasswordField.setMaxWidth(300);
        repeatPasswordField.setMaxHeight(300);

        Button signUpButton = new Button("Sign Up");
        applyButtonStyles(signUpButton);

        Button backButton = new Button("Back");
        applyButtonStyles(backButton);
        backButton.setOnAction(e -> {
            // Call the loginForm method to show the login screen
            //loginForm(primaryStage);
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText(null);
                alert.setContentText("Please log yourself in.");
                // Show the alert
                alert.show();

                // Create a PauseTransition to wait for a specific time before closing the alert and continuing
                PauseTransition pause = new PauseTransition(Duration.millis(2)); // Wait for 2 mseconds
                start(primaryStage);
                pause.setOnFinished(event -> {
                    // Close the alert after the pause
                    alert.close();

                    // Now call the start method or any further action you want to take
                });

                // Start the pause transition
                pause.play();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        signUpButton.setOnAction(e -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String repeatPassword = repeatPasswordField.getText();

            if (!password.equals(repeatPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Failed");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match.");
                alert.showAndWait();
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasminetrivia", "root", "")) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?)");
                stmt.setString(1, email);
                stmt.setString(2, username);
                stmt.setString(3, password);
                stmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sign Up Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created.");
                alert.showAndWait();

                start(primaryStage);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox signUpLayout = new VBox(15);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.getChildren().addAll(
                emailLabel, emailField,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                repeatPasswordLabel, repeatPasswordField,
                signUpButton,
                backButton // Add the Back button below the Sign-Up button
        );

        Image background = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage bgImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
        signUpLayout.setBackground(new Background(bgImage));

        Scene signUpScene = new Scene(signUpLayout);
        primaryStage.setScene(signUpScene);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(signUpScene);
    }

    public void showMainMenu(Stage primaryStage) {

        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);

        // Create a welcome label
        Label welcomeLabel = new Label("Welcome, " + userName + "!");
        welcomeLabel.setFont(customFont);
        welcomeLabel.setTextFill(Color.web("#ffffff"));
        welcomeLabel.setMouseTransparent(true); // Avoid blocking clicks

        // Create buttons for the main menu
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button leaderboardButton = new Button("Scores");
        Button quitButton = new Button("Quit");

        playButton.setOnAction(e -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Play");
                alert.setHeaderText(null);
                alert.setContentText("The game is about to start!");

                // Show the alert
                alert.show();

                // Create a PauseTransition to wait for a specific time before closing the alert and continuing
                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    showCategorySelection(primaryStage);
                    // Close the alert after the pause
                    alert.close();

                });

                // Start the pause transition
                pause.play();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        // Apply styles to buttons
        applyButtonStyles(playButton);
        applyButtonStyles(settingsButton);
        applyButtonStyles(leaderboardButton);
        applyButtonStyles(quitButton);

        // Explicit button sizes
        playButton.setPrefSize(200, 50);
        settingsButton.setPrefSize(200, 50);
        leaderboardButton.setPrefSize(200, 50);
        quitButton.setPrefSize(200, 50);

        // Button actions
        settingsButton.setOnAction(event -> {
            Settings settings = new Settings();
            settings.start(primaryStage);
        });

        quitButton.setOnAction(event -> System.exit(0));

        // Layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER); // Center align elements
        layout.setSpacing(20); // Even spacing
        layout.setPadding(Insets.EMPTY); // Remove unintended padding
        layout.getChildren().addAll(welcomeLabel, playButton, settingsButton, leaderboardButton, quitButton);

        // Set a background image for the layout
        Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false); // Correct scaling
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        layout.setBackground(new Background(bgImage));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 1024, 900);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showCategorySelection(Stage primaryStage) {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 28);

        // Create a title for the category selection screen
        Label titleLabel = new Label("Select a Quiz Category");
        titleLabel.setFont(customFont);
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setAlignment(Pos.CENTER);

        // Create buttons for categories
        Button scienceButton = new Button("Science");
        Button historyButton = new Button("History");
        Button geographyButton = new Button("Geography");
        Button backButton = new Button("Back");

        // Apply styles to buttons
        applyButtonStyles(scienceButton);
        applyButtonStyles(historyButton);
        applyButtonStyles(geographyButton);
        applyButtonStyles(backButton);

        scienceButton.setPrefSize(200, 50);
        historyButton.setPrefSize(200, 50);
        geographyButton.setPrefSize(200, 50);
        backButton.setPrefSize(200, 50);

        scienceButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        historyButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        geographyButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundEffect.playSound("/buttonPress.mp3");
        });

        // Button actions for each category (you can replace these with real logic)
        scienceButton.setOnAction(event -> startQuiz(primaryStage, "Science"));
        historyButton.setOnAction(event -> startQuiz(primaryStage, "History"));
        geographyButton.setOnAction(event -> startQuiz(primaryStage, "Geography"));

        // Back button to return to the main menu
        backButton.setOnAction(event -> showMainMenu(primaryStage));

        // Layout for buttons
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, scienceButton, historyButton, geographyButton, backButton);

        // Set a background image for the layout
        Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        layout.setBackground(new Background(bgImage));

        // Create the scene and set it on the primary stage
        Scene categoryScene = new Scene(layout, 1024, 900);
        primaryStage.setScene(categoryScene);
    }

    // Add this method to handle quiz starting
    private void startQuiz(Stage primaryStage, String category) {
        // TODO: Add logic to load quiz questions based on the selected category
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Start Quiz");
        alert.setHeaderText(null);
        alert.setContentText("Starting quiz in the category: " + category);
        alert.showAndWait();

        // For now, return to the main menu after showing the alert
        showMainMenu(primaryStage);
    }

    // Style method for TextField and PasswordField
    private void styleTextField(TextField textField) {
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

    private void styleTextField(PasswordField passwordField) {
        passwordField.setStyle("-fx-font: 18 \"SourGummy-Light\"; "
                + "-fx-text-fill: white;"
                + "-fx-background-color: #6a4791; "  // Set background color
                + "-fx-background-radius: 10; "
                + "-fx-padding: 12px; "
                + "-fx-border-color: #cccccc; "
                + "-fx-border-width: 2px; "  // Set border thickness
                + "-fx-border-radius: 10; "
                + "-fx-focus-color: #4CAF50; "
                + "-fx-faint-focus-color: transparent;");
    }

    // Button styling (Restored)
    private void applyButtonStyles(Button button) {
        button.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 18));
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
            SoundEffect.playSound("/buttonPress.mp3");
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
