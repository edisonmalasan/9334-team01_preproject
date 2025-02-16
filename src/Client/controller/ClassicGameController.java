package Client.controller;

import Client.connection.ClientConnection;
import common.model.QuestionModel;
import exception.ConnectionException;
import exception.ThreadInterruptedException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ClassicGameController {
    @FXML
    private Label timerLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private HBox choicesBox;
    @FXML
    private ImageView bombImage;
    @FXML
    private ImageView flame;
    @FXML
    private Line wick;

    private List<Button> choiceButtons = new ArrayList<>();
    private ClientConnection clientConnection;
    private List<QuestionModel> questions;
    private int currentQuestionIndex = 0;
    private Timeline wickAnimation;
    private TranslateTransition flameFlicker;
    private Timeline bombTimer;
    private int totalTime = 30;
    private int remainingTime;


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

            Platform.runLater(this::startBombAnimation);
            currentQuestionIndex++;
        } else {
            questionLabel.setText("🎉 Game Over!");
            choicesBox.getChildren().clear();
            bombImage.setVisible(false);
        }
    }

    private void checkAnswer(Button selectedButton, String selectedAnswer, String correctAnswer) {

        if (selectedAnswer.equals(correctAnswer)) {
            selectedButton.setStyle("-fx-background-image: url('/images/correct_answer.png');");
        } else {
            selectedButton.setStyle("-fx-background-image: url('/images/wrong_answer.png');");
            applyPenalty();
        }

        for (Button btn : choiceButtons) {
            if (btn.getText().equals(correctAnswer)) {
                btn.setStyle("-fx-background-image: url('/images/correct_answer.png');");
                break;
            }
        }

        for (Button btn : choiceButtons) {
            btn.setDisable(true);
            btn.setOpacity(0.8);
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(this::showNextQuestion);
            } catch (InterruptedException e) {
                throw new ThreadInterruptedException("Thread was interrupted while waiting to show the next question.", e);
            }
        }).start();
    }

    private void applyPenalty() {
        double wickLength = wick.getEndX() - wick.getStartX();
        double shrinkAmount = wickLength / 3;

        if (wickLength > 0) {
            wick.setStartX(wick.getStartX() + shrinkAmount);
            flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
        }

        int timePenalty = 3;  //reduce time by 3s per mistake
        remainingTime = Math.max(0, remainingTime - timePenalty);
        timerLabel.setText(String.valueOf(remainingTime));

        if (remainingTime <= 0 || wick.getStartX() >= wick.getEndX()) {
            triggerExplosion();
        }
    }

    private void startBombAnimation() {
        bombImage.setVisible(true);
        flame.setVisible(true);
        wick.setVisible(true);
        timerLabel.setText(String.valueOf(remainingTime) + "s");

        remainingTime = totalTime;

        flameFlicker = new TranslateTransition(Duration.millis(200), flame);
        flameFlicker.setFromX(-2);
        flameFlicker.setToX(2);
        flameFlicker.setAutoReverse(true);
        flameFlicker.setCycleCount(Animation.INDEFINITE);
        flameFlicker.play();

        wickAnimation = new Timeline(new KeyFrame(Duration.seconds(1), e -> shortenWick()));
        wickAnimation.setCycleCount(totalTime);
        wickAnimation.play();

        bombTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        bombTimer.setCycleCount(totalTime);
        bombTimer.setOnFinished(e -> triggerExplosion());
        bombTimer.play();
    }

    private void stopBombAnimation() {
        if (wickAnimation != null) wickAnimation.stop();
        if (flameFlicker != null) flameFlicker.stop();
        if (bombTimer != null) bombTimer.stop();
        flame.setVisible(false);
        wick.setVisible(false);
    }

    private void shortenWick() {
        double wickLength = wick.getEndX() - wick.getStartX();
        double shrinkAmount = wickLength / remainingTime;

        if (wick.getStartX() < wick.getEndX()) {
            wick.setStartX(wick.getStartX() + shrinkAmount);
            flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
        }

        if (remainingTime <= 0) {
            stopBombAnimation();
            triggerExplosion();
        }
    }

    private void updateTimer() {
        remainingTime--;
        timerLabel.setText(String.valueOf(remainingTime) + "s");

        if (remainingTime <= 0) {
            bombTimer.stop();
        }
    }

    private void triggerExplosion() {
        stopBombAnimation();
        bombImage.setImage(new Image("/images/explosion.png"));
        System.out.println("BOOM! The bomb explodes!");

        for (Button btn : choiceButtons) {
            btn.setDisable(true);
            btn.setOpacity(0.8);
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(this::showNextQuestion);
            } catch (InterruptedException e) {
                throw new ThreadInterruptedException("Thread was interrupted while waiting to show the next question.", e);
            }
        }).start();
    }
}
