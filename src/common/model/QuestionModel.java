package common.model;

import java.util.List;

public class QuestionModel {
    private String category;
    private String questionText;
    private List<String> choices;
    private String correctAnswer;
    private int score;

    public QuestionModel(String category, String questionText, List<String> choices, String correctAnswer, int score) {
        this.category = category;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Category: " + category + "\nQuestion: " + questionText + "\nChoices: " + choices + "\nAnswer: " + correctAnswer + "\nScore: " + score;
    }
}
