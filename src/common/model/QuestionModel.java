package common.model;

public class QuestionModel {
    private String category;
    private String questionText;
    private String correctAnswer;

    public QuestionModel(String category, String questionText, String correctAnswer) {
        this.category = category;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return "[" + category.toUpperCase() + "] " + questionText + " (Answer: " + correctAnswer + ")";
    }
}
