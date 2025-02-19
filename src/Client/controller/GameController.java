package Client.controller;
/**
 * Contains main game logic
 */
import Client.view.ViewManager;
import common.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.model.ComboModel;
import Client.model.PlayerModel;
import Client.utils.BombUtility;
import Client.utils.QTEUtility;
import common.LoggerSetup;
import common.Response;
import common.model.QuestionModel;
import exception.ConnectionException;
import exception.ThreadInterruptedException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    protected boolean checkMode = false;

    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public GameController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "\nGameController: Error initializing ClientConnection.", e);
        }
    }

    public void setQuestions(String category, List<QuestionModel> questions, boolean isEndlessMode) {
        this.questions = new ArrayList<>(questions);
        this.checkMode = isEndlessMode;
        Collections.shuffle(this.questions);  // Shuffling questions
        this.comboModel = new ComboModel();
        logger.info("\nGameController: Loaded " + questions.size() + " shuffled questions for category: " + category);
        this.bombUtility = new BombUtility(bombImage, flame, wick, timerLabel, this::switchToScoreView, choiceButtons, isEndlessMode);
        this.qteUtility = new QTEUtility(questions.size(), bombUtility::applyPenalty, QTEPane);
        showNextQuestion();
    }

    @FXML
    public void initialize() {
        forfeitButton.setOnAction(this::handleForfeit);
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
            logger.info("\nGameController: Correct answer for question: " + question.getQuestionText());
        } else {
            selectedButton.setStyle("-fx-background-image: url('/images/wrong_answer.png');");
            bombUtility.applyPenalty(3);
            comboModel.resetCombo();
            logger.warning("\nGameController: Wrong answer for question: " + question.getQuestionText());
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
                logger.log(Level.SEVERE, "\nGameController: Thread was interrupted while waiting to show the next question.", e);
                throw new ThreadInterruptedException("Thread was interrupted while waiting to show the next question.", e);
            }
        }).start();
    }

    protected void updateComboUI() {
        logger.info("\nGameController: Updating combo display: " + comboModel.getComboCount());
        Platform.runLater(() -> comboLabel.setText("Combo: " + comboModel.getComboCount()));
    }

    @FXML
    protected void handleForfeit(ActionEvent event) {
        logger.info("\nGameController: Player forfeited. Stopping game...");
        bombUtility.stopBombAnimation();
        finalScore = 0;
        switchToScoreView(event);
    }

    //default event
    protected void switchToScoreView() {
        switchToScoreView(null);
    }

    protected void switchToScoreView(ActionEvent event) {
        sendScoreToServer(finalScore);

        if (event == null) {
            ViewManager.goTo(null, ViewManager.SCORE_VIEW, "Score View", loader -> {
                ScoreController scoreController = loader.getController();
                scoreController.setScore(finalScore);
            });
        } else {
            ViewManager.goTo(event, ViewManager.SCORE_VIEW, "Score View", loader -> {
                ScoreController scoreController = loader.getController();
                scoreController.setScore(finalScore);
            });
        }

        logger.info("\nGameController: Successfully switched to the Score view");
    }

    private void sendScoreToServer(int score) {
        new Thread(() -> {
            try {
                String playerName = InputUsernameController.getPlayerName();
                PlayerModel player = new PlayerModel(playerName, score);

                if (checkMode){
                    player.setName(playerName + "  ");
                }

                clientConnection.sendObject(player);
                Response response = (Response) clientConnection.receiveObject();

                if (response.isSuccess()) {
                    logger.info("\nGameController: Score successfully sent to the server.");
                } else {
                    logger.warning("\nGameController: Failed to send score to the server: " + response.getMessage());
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "\nGameController: Error sending score to the server", e);
            }
        }).start();
    }
}
