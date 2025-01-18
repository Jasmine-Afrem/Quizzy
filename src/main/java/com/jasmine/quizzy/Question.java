package com.jasmine.quizzy;

import java.util.List;

/**
 * Represents a question in the quiz.
 * Contains the question itself, the correct answer, and a list of wrong answers.
 */
public record Question(String question, String correctAnswer, List<String> wrongAnswers) {

    /**
     * Constructs a Question with a specific question text, correct answer, and list of wrong answers.
     *
     * @param question      The question text.
     * @param correctAnswer The correct answer for the question.
     * @param wrongAnswers  A list of incorrect answers for the question.
     */
    public Question {
    }

    /**
     * Returns the text of the question.
     *
     * @return The question text.
     */
    @Override
    public String question() {
        return question;
    }

    /**
     * Returns the correct answer for the question.
     *
     * @return The correct answer.
     */
    @Override
    public String correctAnswer() {
        return correctAnswer;
    }

    /**
     * Returns the list of wrong answers for the question.
     *
     * @return The list of incorrect answers.
     */
    @Override
    public List<String> wrongAnswers() {
        return wrongAnswers;
    }
}