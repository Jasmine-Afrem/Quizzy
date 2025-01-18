package com.jasmine.quizzy;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

import static com.jasmine.quizzy.Main.userName;

/**
 * The {@code Play} class represents a quiz session in the Quizzy application.
 * It handles the loading, parsing, and shuffling of quiz questions based on the specified category.
 * This class manages the user's progress through the quiz, including tracking the current question
 * and the score.
 */
public class Play {

    private final List<Question> questions = new ArrayList<>();
    private int questionIndex = 0;
    private int score = 0;

    /**
     * Constructs a new Play object with the specified category.
     * Loads and shuffles questions from a file based on the given category.
     * @param category The category for the quiz (e.g., 'Science', 'Math').
     * @throws IOException If there is an error reading the question file.
     */
    public Play(String category) throws IOException {
        InputStream is = getClass().getResourceAsStream("/questions/" + category + ".txt");
        assert is != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> lines = reader.lines().toList();
        List<Question> allQuestions = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");  // Use '|' as the delimiter
            if (parts.length == 5) {
                String question = parts[0];
                String correctAnswer = parts[4]; // Correct answer is the last part
                List<String> wrongAnswers = Arrays.asList(parts[1], parts[2], parts[3]); // Wrong answers
                allQuestions.add(new Question(question, correctAnswer, wrongAnswers));
            }
        }
        Collections.shuffle(allQuestions);
        questions.addAll(allQuestions.subList(0, Math.min(10, allQuestions.size())));
    }

    /**
     * Starts the quiz by displaying the first question.
     * Displays the next question or final score based on the current state.
     * @param primaryStage The primary stage for the quiz application.
     */
    public void start(Stage primaryStage) {
        if (questionIndex >= questions.size()) {
            showFinalScore(primaryStage); // Show the final score when questions are finished
        } else {
            Question question = questions.get(questionIndex++);
            StackPane layout = createQuestionLayout(question, primaryStage); // Create a layout for the question
            Scene scene = new Scene(layout, 1024, 900);
            primaryStage.setScene(scene); // Set the updated scene
            layout.requestFocus(); // Focus the layout
        }
    }

    /**
     * Displays the final score of the quiz when all questions are answered.
     * @param primaryStage The primary stage for the quiz application.
     */
    void showFinalScore(Stage primaryStage) {
        // Load the background image
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/background.jpg")).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Create root container with background
        StackPane root = new StackPane();
        root.setBackground(new Background(background));

        // Set up the score label
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);
        Label scoreLabel = new Label("Congratulations! Your score is: " + score + " out of 10.");
        scoreLabel.setTextFill(Color.web("#ffff"));
        scoreLabel.setFont(customFont);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setWrapText(true); // Allow the text to wrap if necessary

        // Create a VBox for the score label and center it in the panel
        VBox scoreBox = new VBox(20);
        scoreBox.setAlignment(Pos.CENTER); // Center the content
        scoreBox.getChildren().add(scoreLabel);

        updateScores();

        // Create the "Back to the main menu" button
        Button playAgainButton = new Button("Back to the main menu!");
        ButtonStyler.applyButtonStyles(playAgainButton); // Apply custom button styling
        playAgainButton.setOnAction(e -> {
            try {
                new MainMenu("User").show(primaryStage); // Navigate to the main menu
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Create a VBox for the button and center it in the panel
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER); // Center the content
        buttonBox.getChildren().add(playAgainButton);

        // Create the dark purple panel and add both the scoreBox and buttonBox
        VBox darkPurplePanel = new VBox(40);
        darkPurplePanel.setAlignment(Pos.CENTER); // Center everything in the panel
        darkPurplePanel.setPrefSize(300, 300); // Adjusted size for the panel
        darkPurplePanel.setMaxHeight(380);
        darkPurplePanel.setMaxWidth(850);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));  // Darker gray shadow

        // Add the scoreBox and buttonBox to the dark purple panel
        darkPurplePanel.getChildren().addAll(scoreBox, buttonBox);

        // Add the dark purple panel to the root container
        root.getChildren().add(darkPurplePanel);

        // Set up the scene and stage
        Scene scene = new Scene(root, 1024, 900);
        primaryStage.setScene(scene);

        // Ensure the background is visible
        scene.setFill(Color.TRANSPARENT); // This ensures the background image is visible behind everything
    }

    /**
     * Updates the stored scores by reading from and writing to the score file.
     */
    private void updateScores() {
        Map<String, Integer> scores = new HashMap<>();
        File file = new File("scor.txt");

        // Read the existing scores from the file
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(":");
                    if (parts.length == 2) {
                        try {
                            scores.put(parts[0], Integer.parseInt(parts[1].trim()));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid score format: " + parts[1]);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Score file not found.");
            }
        }

        // Add the new score to the existing score for the user
        scores.put(userName, scores.getOrDefault(userName, 0) + score);

        // Write the updated scores back to the file
        try (FileWriter writer = new FileWriter(file)) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing scores to file.");
        }
    }

    /**
     * Creates the layout for displaying a question, answers, and timer.
     * @param question The current question to display.
     * @param primaryStage The primary stage for the quiz application.
     * @return The StackPane layout containing the question and answers.
     */
    StackPane createQuestionLayout(Question question, Stage primaryStage) {
        // Load the background image
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/background.jpg")).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Create the main layout with background image
        StackPane root = new StackPane();
        root.setBackground(new Background(background));

        // Load and resize the logo image
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/Quizzy.png")).toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(500);  // Set the logo's width to a smaller size
        logoImageView.setPreserveRatio(true);

        // Create a floating animation for the logo image
        TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
        floatingAnimation.setByY(15);
        floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatingAnimation.setAutoReverse(true);
        floatingAnimation.play(); // Start the floating animation

        // Create the dark purple panel (only for question, answers, and timer)
        VBox darkPurplePanel = new VBox(20);
        darkPurplePanel.setAlignment(Pos.CENTER);
        darkPurplePanel.setPrefSize(350, 400); // Adjusted size for the form
        darkPurplePanel.setMaxWidth(700); // Set maximum width
        darkPurplePanel.setMaxHeight(370);
        darkPurplePanel.setStyle("-fx-background-color: #4e2c6b; -fx-background-radius: 30; -fx-padding: 20;");
        darkPurplePanel.setEffect(new javafx.scene.effect.DropShadow(10, Color.DARKGRAY));  // Darker gray shadow

        // Load the custom font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 30);

        // Set up the question label
        Label questionLabel = new Label(question.question());
        questionLabel.setFont(customFont);
        questionLabel.setTextFill(Color.web("#ffffff"));
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        // Timer label
        Label timerLabel = new Label();
        timerLabel.setFont(customFont);
        timerLabel.setTextFill(Color.web("white"));

        // Initialize countdown variable
        final int[] timeRemaining = {20}; // 20 seconds countdown
        timerLabel.setText("Time: " + timeRemaining[0] + " seconds");

        // Timer thread variable
        final boolean[] timerRunning = {true};  // Flag to track if the countdown should keep running

        // Update the timer every second using a simple delay loop
        Thread countdownThread = new Thread(() -> {
            try {
                while (timeRemaining[0] > 0 && timerRunning[0]) {
                    Thread.sleep(1000); // Wait for 1 second
                    timeRemaining[0]--; // Decrease the time by 1 second
                    Platform.runLater(() -> timerLabel.setText("Time: " + timeRemaining[0] + " seconds"));
                }
                // When the timer reaches 0 and we are still not at the last question
                if (timeRemaining[0] == 0) {
                    Platform.runLater(() -> handleTimeOut(question, primaryStage)); // Handle time out
                }
            } catch (InterruptedException e) {
                // Handle the interruption gracefully, no need to print the stack trace
                Thread.currentThread().interrupt();
            }
        });
        countdownThread.setDaemon(true); // Make the thread a daemon thread so it doesn't block shutdown
        countdownThread.start();

        // Prepare the answers list
        List<String> allAnswers = new ArrayList<>(question.wrongAnswers());
        allAnswers.add(question.correctAnswer());
        Collections.shuffle(allAnswers);

        // Measure the width of the longest answer
        double maxWidth = 10;
        for (String answer : allAnswers) {
            Text text = new Text(answer);
            text.setFont(customFont);
            double width = text.getBoundsInLocal().getWidth();
            maxWidth = Math.max(maxWidth, width);
        }

        // Add padding to the maximum width
        maxWidth += 60; // Add some padding for better aesthetics

        // Create two VBoxes for the left and right columns
        VBox leftColumn = new VBox(30);
        leftColumn.setAlignment(Pos.CENTER);

        VBox rightColumn = new VBox(30);
        rightColumn.setAlignment(Pos.CENTER);


        // Split the answers between the two columns
        for (int i = 0; i < allAnswers.size(); i++) {
            String answer = allAnswers.get(i); // Capture the value of the current answer
            Button answerButton = new Button(answer);
            ButtonStyler.applyButtonStyles(answerButton); // Apply custom button styling

            // Set the preferred width for all buttons
            answerButton.setPrefWidth(maxWidth);

            // Stop the countdown thread when the user selects an answer
            answerButton.setOnAction(e -> {
                timerRunning[0] = false; // Stop the countdown thread when an answer is selected
                countdownThread.interrupt(); // Stop the countdown when the answer is clicked
                handleAnswer(answer, question, primaryStage); // Handle the answer selection
            });

            if (i % 2 == 0) {
                leftColumn.getChildren().add(answerButton); // Add to left column
            } else {
                rightColumn.getChildren().add(answerButton); // Add to right column
            }
        }

        // Combine the two columns in an HBox
        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(leftColumn, rightColumn);

        // Add the question label, timer label, and buttons to the dark purple panel
        darkPurplePanel.getChildren().addAll(questionLabel, buttonBox, timerLabel);

        // Create the main layout that includes the logo and the dark purple panel
        VBox mainLayout = new VBox(20, logoImageView, darkPurplePanel);
        mainLayout.setAlignment(Pos.CENTER);

        // Add the main layout to the root
        root.getChildren().add(mainLayout);

        return root;
    }

    /**
     * Handles the timeout event when the timer reaches zero.
     * Displays an alert with the correct answer and proceeds to the next question.
     * @param question The current question.
     * @param primaryStage The primary stage for the quiz application.
     */
    void handleTimeOut(Question question, Stage primaryStage) {
        // Handle the timeout event when time runs out
        SoundEffect.playSound("/sounds/wrongAnswer.mp3");

        // Show the correct answer in a custom alert
        Platform.runLater(() -> {
            String message = "Time's up! The correct answer was: " + question.correctAnswer();
            CustomAlert.showAlert(message);
            updateQuestionLayout(primaryStage); // After the alert is closed, move to the next question
        });
    }

    /**
     * Handles the user's answer to a question.
     * Updates the score and either moves to the next question or shows the final score.
     * @param answer The user's answer.
     * @param question The current question.
     * @param primaryStage The primary stage for the quiz application.
     */
    void handleAnswer(String answer, Question question, Stage primaryStage) {
        if (answer.equals(question.correctAnswer())) {
            score++;
            SoundEffect.playSound("/sounds/correctAnswer.mp3");

            // Check if it's the last question, and transition smoothly
            if (questionIndex >= questions.size()) {
                showFinalScore(primaryStage);  // Show final score
            } else {
                // Delay moving to the next question to allow sound to finish
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> updateQuestionLayout(primaryStage));  // Proceed to next question after delay
                pause.play();
            }
        } else {
            SoundEffect.playSound("/sounds/wrongAnswer.mp3");
            String message = "Wrong Answer! The correct answer was: " + question.correctAnswer();
            CustomAlert.showAlert(message);
            updateQuestionLayout(primaryStage); // Proceed to next question after alert
        }
    }

    /**
     * Updates the layout for the next question or displays the final score if there are no more questions.
     * @param primaryStage The primary stage for the quiz application.
     */
    private void updateQuestionLayout(Stage primaryStage) {
        if (questionIndex >= questions.size()) {
            showFinalScore(primaryStage);  // Show the final score when questions are finished
        } else {
            Question question = questions.get(questionIndex++);
            StackPane layout = createQuestionLayout(question, primaryStage); // Create a layout for the question
            Scene scene = primaryStage.getScene();
            if (scene != null) {
                // Update the StackPane root with the new layout
                ((StackPane) scene.getRoot()).getChildren().setAll(layout.getChildren());
            } else {
                Scene newScene = new Scene(layout, 1024, 900);
                primaryStage.setScene(newScene);
            }
            primaryStage.show();
        }
    }
}
