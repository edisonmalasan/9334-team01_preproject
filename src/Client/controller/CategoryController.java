package Client.controller;

import Client.connection.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.view.CategoryView;
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
        logger.info("📩 Requesting question for category: " + category);

        new Thread(() -> {
            try {
                QuestionModel question = clientConnection.getQuestion(category);

                if (question != null) {
                    logger.info("✅ Received Question: " + question.getQuestionText());
                    updateUI(() -> switchToGameplay(category, event));
                } else {
                    logger.warning("⚠️ No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "❌ Failed to fetch question from server.", e);
            }
        }).start();
    }

    private void switchToGameplay(String category, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/classic_game.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Game - " + category);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "❌ Failed to switch to gameplay screen.", e);
        }
    }

    private void updateUI(Runnable action) {
        Platform.runLater(action);
    }

    public static String getSelectedCategory() {
        return selectedCategory;
    }
}
