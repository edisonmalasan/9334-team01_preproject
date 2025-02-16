package Client.controller;

import Client.connection.ClientConnection;
import common.model.QuestionModel;
import exception.ConnectionException;
import exception.ThreadInterruptedException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ClassicGameController {
    @FXML
    private Label questionLabel;
    @FXML
    private HBox choicesBox;

    private List<Button> choiceButtons = new ArrayList<>();

    private ClientConnection clientConnection;
    private List<QuestionModel> questions;
    private int currentQuestionIndex = 0;

    public ClassicGameController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void setQuestions(String category, List<QuestionModel> questions) {
        this.questions = questions;
        System.out.println("DEBUG: Loaded " + questions.size() + " questions for category: " + category);
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            QuestionModel question = questions.get(currentQuestionIndex);
            questionLabel.setText(question.getQuestionText());
            choicesBox.getChildren().clear();
            choiceButtons.clear();

            for (String choice : question.getChoices()) {
                Button choiceButton = new Button(choice);
                choiceButton.setPrefSize(146, 50);
                choiceButton.setStyle("-fx-font-family: 'Roboto Mono'; -fx-font-size: 15px;");
                choiceButton.setOnAction(e -> checkAnswer(choiceButton, choice, question.getCorrectAnswer()));
                choicesBox.getChildren().add(choiceButton);
                choiceButtons.add(choiceButton);
            }

            currentQuestionIndex++;
        } else {
            questionLabel.setText("🎉 Game Over!");
            choicesBox.getChildren().clear();
        }
    }

    private void checkAnswer(Button selectedButton, String selectedAnswer, String correctAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            selectedButton.setStyle("-fx-background-image: url('/images/correct_answer.png');");
        } else {
            selectedButton.setStyle("-fx-background-image: url('/images/wrong_answer.png');");
        }

        // show the correct answer if a wrong answer was chosen
        for (Button btn : choiceButtons) {
            if (btn.getText().equals(correctAnswer)) {
                btn.setStyle("-fx-background-image: url('/images/correct_answer.png');");
                break;
            }
        }

        // disable all buttons after answering
        for (Button btn : choiceButtons) {
            btn.setDisable(true);
            btn.setOpacity(0.8);
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(this::showNextQuestion);
            } catch (InterruptedException e) {
                throw new ThreadInterruptedException("Thread was interrupted while waiting to show the next question.", e);
            }
        }).start();
    }


}
