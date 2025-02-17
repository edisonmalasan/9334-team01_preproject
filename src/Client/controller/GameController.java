package Client.controller;

import Client.connection.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.model.ComboModel;
import Client.model.PlayerModel;
import Client.utils.BombUtility;
import Client.utils.QTEUtility;
import common.Response;
import common.model.QuestionModel;
import exception.ConnectionException;
import exception.ThreadInterruptedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GameController {
    @FXML
    protected Label timerLabel;
    @FXML
    protected Label questionLabel;
    @FXML
    protected Label comboLabel;
    @FXML
    protected HBox choicesBox;
    @FXML
    protected ImageView bombImage;
    @FXML
    protected ImageView flame;
    @FXML
    protected Line wick;
    @FXML
    protected Pane QTEPane;
    @FXML
    protected Button forfeitButton;

    protected List<Button> choiceButtons = new ArrayList<>();
    protected ClientConnection clientConnection;
    protected List<QuestionModel> questions;
    protected int currentQuestionIndex = 0;
    protected QTEUtility qteUtility;
    protected BombUtility bombUtility;
    protected ComboModel comboModel;
    protected int finalScore = 0;

    protected static final Logger logger = Logger.getLogger(GameController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public GameController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void setQuestions(String category, List<QuestionModel> questions) {
        this.questions = questions;
        this.comboModel = new ComboModel();
        System.out.println("DEBUG: Loaded " + questions.size() + " questions for category: " + category);
        this.bombUtility = new BombUtility(bombImage, flame, wick, timerLabel, this::switchToScoreView, choiceButtons);
        this.qteUtility = new QTEUtility(questions.size(), bombUtility::applyPenalty, QTEPane);
        showNextQuestion();
    }

    @FXML
    public void initialize() {
        forfeitButton.setOnAction(e -> handleForfeit());
    }

    protected abstract void showNextQuestion();

    protected void checkAnswer(Button selectedButton, String selectedAnswer, QuestionModel question) {
        int questionScore = question.getScore();

        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            selectedButton.setStyle("-fx-background-image: url('/images/correct_answer.png');");

            // Added combo multiplier to the score
            int comboMultiplier = Math.max(1, comboModel.getComboCount()); // Ensure at least x1 multiplier
            int totalScoreForQuestion = questionScore * comboMultiplier;
            finalScore += totalScoreForQuestion;

            comboModel.incrementCombo();
        } else {
            selectedButton.setStyle("-fx-background-image: url('/images/wrong_answer.png');");
            bombUtility.applyPenalty(3);
            comboModel.resetCombo();
        }

        updateComboUI();

        for (Button btn : choiceButtons) {
            if (btn.getText().equals(question.getCorrectAnswer())) {
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

    protected void updateComboUI() {
        System.out.println("DEBUG: Updating combo display: " + comboModel.getComboCount());
        Platform.runLater(() -> comboLabel.setText("Combo: " + comboModel.getComboCount()));
    }

    @FXML
    protected void handleForfeit() {
        System.out.println("Player forfeited. Stopping game...");
        bombUtility.stopBombAnimation();
        finalScore = 0;
        switchToScoreView();
    }

    protected void switchToScoreView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/score_view.fxml"));
            Parent root = loader.load();

            ScoreController scoreController = loader.getController();
            scoreController.setScore(finalScore);

            // Send the player's score to the server
            sendScoreToServer(finalScore);

            Stage stage = (Stage) timerLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Score");
            stage.setResizable(false);
            stage.show();

            logger.info("Successfully switched to the Score view");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Score view", e);
            throw new RuntimeException(e);
        }
    }

    private void sendScoreToServer(int score) {
        new Thread(() -> {
            try {
                String playerName = InputUsernameController.getPlayerName();
                PlayerModel player = new PlayerModel(playerName, score);

                clientConnection.sendObject(player);
                Response response = (Response) clientConnection.receiveObject();

                if (response.isSuccess()) {
                    logger.info("Score successfully sent to the server.");
                } else {
                    logger.warning("Failed to send score to the server: " + response.getMessage());
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Error sending score to the server", e);
            }
        }).start();
    }
}