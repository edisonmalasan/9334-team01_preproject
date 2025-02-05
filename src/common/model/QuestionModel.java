package common.model;

import java.util.List;

public class QuestionModel {
    private String category;
    private String questionText;
    private List<String> choices;
    private String correctAnswer;

    public QuestionModel(String category, String questionText, List<String> choices, String correctAnswer) {
        this.category = category;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
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

    @Override
    public String toString() {
        return "Category: " + category + "\nQuestion: " + questionText + "\nChoices: " + choices + "\nAnswer: " + correctAnswer;
    }
}
