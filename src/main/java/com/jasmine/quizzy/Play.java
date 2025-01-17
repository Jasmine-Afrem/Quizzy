    package com.jasmine.quizzy;
    
    import javafx.animation.KeyFrame;
    import javafx.animation.PauseTransition;
    import javafx.animation.Timeline;
    import javafx.animation.TranslateTransition;
    import javafx.application.Platform;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.scene.text.Text;
    import javafx.scene.text.TextAlignment;
    import javafx.stage.Stage;
    import javafx.util.Duration;
    
    import java.io.*;
    import java.util.*;
    import java.util.stream.Collectors;
    
    import static com.jasmine.quizzy.Main.userName;
    
    public class Play {
    
        private final String category;
        private final List<Question> questions = new ArrayList<>();
        private int questionIndex = 0;
        private int score = 0;
    
        public Play(String category) throws IOException {
            this.category = category;
            InputStream is = getClass().getResourceAsStream("/questions/" + category + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<String> lines = reader.lines().collect(Collectors.toList());
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

        public void start(Stage primaryStage) {
            if (questionIndex >= questions.size()) {
                showFinalScore(primaryStage); // Show the final score when questions are finished
                return;
            } else {
                Question question = questions.get(questionIndex++);
                VBox layout = createQuestionLayout(question, primaryStage); // Create a layout for the question
                Scene scene = new Scene(layout, 1000, 800);
                primaryStage.setScene(scene); // Set the updated scene
                layout.requestFocus(); // Focus the layout
            }
        }

        private void showFinalScore(Stage primaryStage) {
            // Load the background image
            Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());
    
            // Create a BackgroundSize object (to scale the image appropriately)
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
    
            // Create a BackgroundImage object
            BackgroundImage background = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize);
    
            // Create VBox layout and set the background
            VBox layout = new VBox(30);
            layout.setBackground(new Background(background));
            layout.setAlignment(Pos.CENTER);
    
            // Set up the score label
            Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 34);
            Label scoreLabel = new Label("Congratulations! Your score is: " + score + " out of 10.");
            scoreLabel.setTextFill(Color.web("#ffff"));
            scoreLabel.setFont(customFont);
            scoreLabel.setAlignment(Pos.CENTER);
    
            // Update the high scores
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
    
            // Add elements to the layout
            layout.getChildren().addAll(scoreLabel, playAgainButton);
    
            // Set up the scene and stage
            Scene scene = new Scene(layout, 1000, 800);
            primaryStage.setScene(scene);
        }

    
        private void updateScores() {
            Map<String, Integer> scores = new HashMap<>();
            File file = new File("scor.txt");
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String[] parts = scanner.nextLine().split(":");
                        if (parts.length == 2) {
                            scores.put(parts[0], Integer.parseInt(parts[1]));
                        }
                    }
                } catch (FileNotFoundException e) {
                    // Handle error
                }
            }
    
            if (!scores.containsKey(userName) || score > scores.get(userName)) {
                scores.put(userName, score); // Update if the score is higher
            }
    
            try (FileWriter writer = new FileWriter(file)) {
                for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error writing scores to file.");
            }
        }

        private VBox createQuestionLayout(Question question, Stage primaryStage) {
            // Load the background image
            Image backgroundImage = new Image(getClass().getResource("/background.jpg").toExternalForm());

            // Create a BackgroundSize object (to scale the image appropriately)
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);

            // Create a BackgroundImage object
            BackgroundImage background = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize);

            // Create VBox layout and set the background
            VBox layout = new VBox(40); // Use more spacing between elements
            layout.setBackground(new Background(background));
            layout.setAlignment(Pos.CENTER);

            // Load and resize the logo image
            Image logoImage = new Image(getClass().getResource("/Quizzy.png").toExternalForm());
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(500);  // Set the logo's width to a smaller size
            logoImageView.setPreserveRatio(true);

            // Create a floating animation for the logo image
            TranslateTransition floatingAnimation = new TranslateTransition(Duration.seconds(2), logoImageView);
            floatingAnimation.setByY(15);
            floatingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
            floatingAnimation.setAutoReverse(true);
            floatingAnimation.play(); // Start the floating animation

            // Add the logo image to the layout (first item in the VBox)
            layout.getChildren().add(logoImageView);

            // Load the custom font
            Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 30);

            // Set up the question label
            Label questionLabel = new Label(question.getQuestion());
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
                        Platform.runLater(() -> {
                            timerLabel.setText("Time: " + timeRemaining[0] + " seconds");
                        });
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
            List<String> allAnswers = new ArrayList<>(question.getWrongAnswers());
            allAnswers.add(question.getCorrectAnswer());
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

            // Add the question label and buttons to the layout first
            layout.getChildren().addAll(questionLabel, buttonBox);

            // Then add the timer label below the answers
            layout.getChildren().add(timerLabel);

            return layout;
        }

        private void handleTimeOut(Question question, Stage primaryStage) {
            // Handle the timeout event when time runs out
            SoundEffect.playSound("/sounds/wrongAnswer.mp3");

            // Show the correct answer in a popup alert
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Time Out");
                alert.setHeaderText(null);
                alert.setContentText("Time is up! The correct answer was: " + question.getCorrectAnswer());
                alert.setOnHidden(evt -> updateQuestionLayout(primaryStage)); // After the alert is closed, move to the next question
                alert.showAndWait(); // Show the alert and block further actions until it's dismissed
            });
        }

        private void handleAnswer(String answer, Question question, Stage primaryStage) {
            if (answer.equals(question.getCorrectAnswer())) {
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wrong Answer");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Answer! The correct answer was: " + question.getCorrectAnswer());
                alert.setOnHidden(evt -> updateQuestionLayout(primaryStage)); // Proceed to next question after alert
                alert.showAndWait();
            }
        }

        private void updateQuestionLayout(Stage primaryStage) {
            if (questionIndex >= questions.size()) {
                showFinalScore(primaryStage);  // Show the final score when questions are finished
                return;
            } else {
                Question question = questions.get(questionIndex++);
                VBox layout = createQuestionLayout(question, primaryStage); // Create a layout for the question
                Scene scene = primaryStage.getScene();
                if (scene != null) {
                    // Reuse the existing scene and update its layout
                    ((VBox) scene.getRoot()).getChildren().setAll(layout.getChildren());
                } else {
                    Scene newScene = new Scene(layout, 1000, 800);
                    primaryStage.setScene(newScene);
                }
                primaryStage.show();
            }
        }
    }
