package Client.controller;

import Client.connection.ClientConnection;
import Client.view.CategoryView;
import common.model.QuestionModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryController {
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
    
    public void setCategoryView(CategoryView categoryView) {
        this.categoryView = categoryView;
    }


    public CategoryController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        algebraButton.setOnAction(event -> requestQuestionFromServer("Algebra"));
        anglesButton.setOnAction(event -> requestQuestionFromServer("Angles"));
        geometryButton.setOnAction(event -> requestQuestionFromServer("Geometry"));
        logicButton.setOnAction(event -> requestQuestionFromServer("Logic"));
        probabilityButton.setOnAction(event -> requestQuestionFromServer("Probability"));
        trigoButton.setOnAction(event -> requestQuestionFromServer("Trigonometry"));
    }

    private void requestQuestionFromServer(String category) {
        selectedCategory = category;
        System.out.println("DEBUG: Requesting question for category: " + category);

        new Thread(() -> {
            try {
                QuestionModel question = clientConnection.getQuestion(category);

                if (question != null) {
                    System.out.println("DEBUG: Received Question: " + question.getQuestionText());
                    updateUI(() -> switchToGameplay(category));
                } else {
                    System.out.println("ERROR: No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("ERROR: Failed to fetch question from server.");
            }
        }).start();
    }

    private void switchToGameplay(String category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/gameplay.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) algebraButton.getScene().getWindow();
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
