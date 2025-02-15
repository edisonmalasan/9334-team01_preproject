package Client.controller;

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

import javax.swing.*;
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
        algebraButton.setOnAction(event -> requestQuestionFromServer("ALGEBRA", event));
        anglesButton.setOnAction(event -> requestQuestionFromServer("ARITHMETIC", event)); // ANGLES
        geometryButton.setOnAction(event -> requestQuestionFromServer("GEOMETRY", event));
        logicButton.setOnAction(event -> requestQuestionFromServer("LOGIC", event));
        probabilityButton.setOnAction(event -> requestQuestionFromServer("PROBABILITY", event));
        trigoButton.setOnAction(event -> requestQuestionFromServer("TRIGONOMETRY", event));
    }

    private void requestQuestionFromServer(String category, ActionEvent event) {
        selectedCategory = category;
        System.out.println("DEBUG: Requesting question for category: " + category);

        new Thread(() -> {
            try {
                QuestionModel question = clientConnection.getQuestion(category);

                if (question != null) {
                    System.out.println("DEBUG: Received Question: " + question.getQuestionText());
                    updateUI(() -> switchToGameplay(category, event));
                } else {
                    System.out.println("ERROR: No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("ERROR: Failed to fetch question from server.");
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
