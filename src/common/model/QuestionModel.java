package common.model;

public class QuestionModel {
    private String difficulty;
    private String questionText;
    private String correctAnswer;

    public QuestionModel(String difficulty, String questionText, String correctAnswer) {
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return "[" + difficulty.toUpperCase() + "] " + questionText + " (Answer: " + correctAnswer + ")";
    }
}
