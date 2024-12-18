package com.jasmine.quizzy;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.Media;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
            String[] parts = line.split(",");
            String question = parts[0];
            String correctAnswer = parts[4];
            List<String> wrongAnswers = Arrays.asList(parts[1], parts[2], parts[3]);
            allQuestions.add(new Question(question, correctAnswer, wrongAnswers));
        }
        Collections.shuffle(allQuestions);
        questions.addAll(allQuestions.subList(0, Math.min(10, allQuestions.size())));
    }

    public void start(Stage primaryStage) {
        String buttonStyle = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #4CAF50;";
        String buttonHover = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #45a049;";
        String buttonPressed = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #216a28;";
        String labelColor = "#D0E8F2";

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

    // Show the final score when all questions have been answered
    private void showFinalScore(Stage primaryStage) {
        Label scoreLabel = new Label("Congratulations! Your score is: " + score + " out of 10.");
        scoreLabel.setTextFill(Color.web("#D0E8F2"));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        scoreLabel.setAlignment(Pos.CENTER);

        // Update the high scores
        updateScores();

        Button playAgainButton = new Button("Inapoi la meniul principal!");
        playAgainButton.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #4CAF50;");
        playAgainButton.setAlignment(Pos.CENTER);
        playAgainButton.setOnAction(e -> {
            try {
                new MainMenu("User").show(primaryStage); // Navigate to the main menu
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scoreLabel, playAgainButton);
        Scene scene = new Scene(layout, 1000, 800);
        primaryStage.setScene(scene);
    }

    // Update the scores in the file
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
            System.out.println("Nu s-a putut scrie în fișierul de scoruri.");
        }
    }

    // Create the layout for the current question
    private VBox createQuestionLayout(Question question, Stage primaryStage) {
        String buttonStyle = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #4CAF50;";
        String buttonHover = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #45a049;";
        String buttonPressed = "-fx-font: 16 arial; -fx-base: #b6e7c9; -fx-background-color: #216a28;";
        String labelColor = "#D0E8F2";

        Label questionLabel = new Label(question.getQuestion());
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        questionLabel.setTextFill(Color.web(labelColor));
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        List<String> allAnswers = new ArrayList<>(question.getWrongAnswers());
        allAnswers.add(question.getCorrectAnswer());
        Collections.shuffle(allAnswers);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().add(questionLabel);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        for (String answer : allAnswers) {
            Button answerButton = new Button(answer);
            answerButton.setStyle(buttonStyle);
            answerButton.setOnMouseEntered(e -> answerButton.setStyle(buttonHover));
            answerButton.setOnMouseExited(e -> answerButton.setStyle(buttonStyle));
            answerButton.setOnMousePressed(e -> answerButton.setStyle(buttonPressed));
            answerButton.setOnMouseReleased(e -> answerButton.setStyle(buttonStyle));
            answerButton.setWrapText(true);
            answerButton.setTextAlignment(TextAlignment.CENTER);
            answerButton.setMinWidth(200);
            answerButton.setMaxWidth(300);
            answerButton.setOnAction(e -> handleAnswer(answer, question, primaryStage));

            buttonBox.getChildren().add(answerButton);
        }

        layout.getChildren().add(buttonBox);
        return layout;
    }

    // Handle the user's answer
    private void handleAnswer(String answer, Question question, Stage primaryStage) {
        if (answer.equals(question.getCorrectAnswer())) {
            score++;
            playSound("correctAnswer.mp3");
        } else {
            playSound("wrongAnswer.mp3");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Raspuns Gresit");
            alert.setHeaderText(null);
            alert.setContentText("Raspuns gresit! Raspunsul corect a fost: " + question.getCorrectAnswer());
            alert.setOnHidden(evt -> start(primaryStage)); // Move to the next question after the alert is closed
            alert.showAndWait();
            return;
        }

        // Move to the next question after handling the answer
        start(primaryStage);
    }

    private void playSound(String soundFileName) {
        URL soundFileUrl = getClass().getResource("/" + soundFileName);
        Media sound = new Media(soundFileUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(Settings.getVolum());
        mediaPlayer.play();
    }
}

