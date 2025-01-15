package com.jasmine.quizzy;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 24);
        Label scoreLabel = new Label("Congratulations! Your score is: " + score + " out of 10.");
        scoreLabel.setTextFill(Color.web("#D0E8F2"));
        scoreLabel.setFont(customFont);
        scoreLabel.setAlignment(Pos.CENTER);

        // Update the high scores
        updateScores();

        Button playAgainButton = new Button("Back to the main menu!");
        ButtonStyler.applyButtonStyles(playAgainButton); // Apply custom button styling
        playAgainButton.setOnAction(e -> {
            try {
                new MainMenu("User").show(primaryStage); // Navigate to the main menu
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scoreLabel, playAgainButton);
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
        VBox layout = new VBox(10);
        layout.setBackground(new Background(background));
        layout.setAlignment(Pos.CENTER);

        // Load the custom font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SourGummy-Medium.ttf"), 30);

        // Set up the question label
        Label questionLabel = new Label(question.getQuestion());
        questionLabel.setFont(customFont);
        questionLabel.setTextFill(Color.web("#ffffff"));
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        // Prepare the answers list
        List<String> allAnswers = new ArrayList<>(question.getWrongAnswers());
        allAnswers.add(question.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        // Add the question label to the layout
        layout.getChildren().add(questionLabel);

        // Set up buttons for answers
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        for (String answer : allAnswers) {
            Button answerButton = new Button(answer);
            ButtonStyler.applyButtonStyles(answerButton);  // Apply custom button styling
            answerButton.setOnAction(e -> handleAnswer(answer, question, primaryStage));
            buttonBox.getChildren().add(answerButton);
        }

        // Add the answer buttons to the layout
        layout.getChildren().add(buttonBox);

        return layout;
    }

    private void handleAnswer(String answer, Question question, Stage primaryStage) {
        if (answer.equals(question.getCorrectAnswer())) {
            score++;
            SoundEffect.playSound("/sounds/correctAnswer.mp3");

            // Delay moving to the next question to allow for sound playback
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> updateQuestionLayout(primaryStage));
            pause.play();
        } else {
            SoundEffect.playSound("/sounds/wrongAnswer.mp3");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong Answer");
            alert.setHeaderText(null);
            alert.setContentText("Wrong Answer! The correct answer was: " + question.getCorrectAnswer());
            alert.setOnHidden(evt -> updateQuestionLayout(primaryStage));
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
