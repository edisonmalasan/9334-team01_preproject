package Client.controller;

import Client.view.ViewManager;
import Server.handler.ClientHandler;
import common.AnsiFormatter;
import Client.connection.ClientConnection;
import common.LoggerSetup;
import common.Response;
import common.model.QuestionModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController {
    @FXML
    public Button returnButton;

    @FXML
    private Button algebraButton;
    @FXML
    private Button anglesButton;
    @FXML
    private Button geometryButton;
    @FXML
    private Button logicButton;
    @FXML
    private Button probabilityButton;
    @FXML
    private Button trigoButton;

    private ClientConnection clientConnection;
    private static String selectedCategory;
    private boolean isEndlessMode = false;

//    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");
    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public CategoryController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Failed to initialize ClientConnection.", e);
        }
    }

    @FXML
    public void initialize() {
        algebraButton.setOnAction(event -> requestQuestionFromServer("ALGEBRA", event));
        anglesButton.setOnAction(event -> requestQuestionFromServer("ARITHMETIC", event)); // ANGLES
        geometryButton.setOnAction(event -> requestQuestionFromServer("GEOMETRY", event));
        logicButton.setOnAction(event -> requestQuestionFromServer("LOGIC", event));
        probabilityButton.setOnAction(event -> requestQuestionFromServer("PROBABILITY", event));
        trigoButton.setOnAction(event -> requestQuestionFromServer("TRIGONOMETRY", event));
        returnButton.setOnAction(event -> ViewManager.goTo(event, ViewManager.MAIN_MENU, "Bomb Defusing Game"));
    }

    private void requestQuestionFromServer(String category, ActionEvent event) {
        selectedCategory = category;
        logger.info("\nCategoryController: Requesting questions for category: " + category);

        new Thread(() -> {
            try {
                clientConnection.sendObject("GET_QUESTION:" + category);
                Response response = (Response) clientConnection.receiveObject();

                logger.info("\nCategoryController: DEBUG: Received response from server: " + response);

                if (response.isSuccess() && response.getData() instanceof List) {
                    List<QuestionModel> questions = (List<QuestionModel>) response.getData();

                    logger.info("CategoryController: Received " + questions.size() + " questions for category: " + category);

                    // log formatted questions for debug purpose
                    for (int i = 0; i < questions.size(); i++) {
                        QuestionModel q = questions.get(i);
                        logger.info("\nCategoryController: \n📌 Question " + (i + 1) + ":\n"
                                + "   🏷 Category: " + q.getCategory() + "\n"
                                + "   ❓ Question: " + q.getQuestionText() + "\n"
                                + "   🔢 Choices: " + q.getChoices() + "\n"
                                + "   ✅ Correct Answer: " + q.getCorrectAnswer() + "\n"
                                + "   ⭐ Score: " + q.getScore() + "\n"
                                + "--------------------------------------");
                    }

                    updateUI(() -> switchToGameplay(category, questions, event, isEndlessMode));
                } else {
                    logger.warning("CategoryController: No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "CategoryController: Failed to fetch questions from server.", e);
            }
        }).start();
    }

    private void switchToGameplay(String category, List<QuestionModel> questions, ActionEvent event, boolean isEndlessMode) {
        if (isEndlessMode) {
            // use ViewManager to switch to Endless Game
            ViewManager.goTo(event, ViewManager.ENDLESS_GAME, "Endless Mode Game", loader -> {
                GameController gameController = loader.getController();
                gameController.setQuestions(category, questions, true);
            });
            logger.info("\nCategoryController: Switched to Endless Mode gameplay.");
        } else {
            // use ViewManager to switch to Classic Game
            ViewManager.goTo(event, ViewManager.CLASSIC_GAME, "Classic Mode Game", loader -> {
                GameController gameController = loader.getController();
                gameController.setQuestions(category, questions, false);
            });


            logger.info("\nCategoryController: Switched to Classic Mode gameplay.");
        }
    }

    private void updateUI(Runnable action) {
        Platform.runLater(action);
    }

    public void setEndlessMode(boolean isEndlessMode) {
        this.isEndlessMode = isEndlessMode;
    }
}
