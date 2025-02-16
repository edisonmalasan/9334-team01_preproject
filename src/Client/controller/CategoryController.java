package Client.controller;

import Client.connection.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.view.CategoryView;
import common.Response;
import common.model.QuestionModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController {
    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

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
    private CategoryView categoryView;

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public void setCategoryView(CategoryView categoryView) {
        this.categoryView = categoryView;
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
    }

    private void requestQuestionFromServer(String category, ActionEvent event) {
        selectedCategory = category;
        logger.info("📩 Requesting questions for category: " + category);

        new Thread(() -> {
            try {
                clientConnection.sendObject("GET_QUESTION:" + category);
                Response response = (Response) clientConnection.receiveObject();

                logger.info("DEBUG: Received response from server: " + response);

                if (response.isSuccess() && response.getData() instanceof List) {
                    List<QuestionModel> questions = (List<QuestionModel>) response.getData();

                    logger.info("✅ Received " + questions.size() + " questions for category: " + category);

                    // log formatted questions for debug purpose
                    for (int i = 0; i < questions.size(); i++) {
                        QuestionModel q = questions.get(i);
                        logger.info("\n📌 Question " + (i + 1) + ":\n"
                                + "   🏷 Category: " + q.getCategory() + "\n"
                                + "   ❓ Question: " + q.getQuestionText() + "\n"
                                + "   🔢 Choices: " + q.getChoices() + "\n"
                                + "   ✅ Correct Answer: " + q.getCorrectAnswer() + "\n"
                                + "   ⭐ Score: " + q.getScore() + "\n"
                                + "--------------------------------------");
                    }

                    updateUI(() -> switchToGameplay(category, questions, event));
                } else {
                    logger.warning("⚠️ No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "❌ Failed to fetch questions from server.", e);
            }
        }).start();
    }



    private void switchToGameplay(String category, List<QuestionModel> questions, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/classic_game.fxml"));
            Parent root = loader.load();

            ClassicGameController gameController = loader.getController();
            gameController.setQuestions(category, questions); //  pass all questions

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Game - " + category);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(Runnable action) {
        Platform.runLater(action);
    }

    public static String getSelectedCategory() {
        return selectedCategory;
    }
}
