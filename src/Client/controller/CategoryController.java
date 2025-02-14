package Client.controller;

import Client.connection.ClientConnection;
import Client.view.CategoryView;
import common.model.QuestionModel;
import exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryController {
    private ClientConnection clientConnection;
    private CategoryView categoryView;
    private static String selectedCategory;

    @FXML
    private Button algebraButton, anglesButton, geometryButton, logicButton, probabilityButton, trigoButton;

    public void setCategoryView(CategoryView categoryView) {
        this.categoryView = categoryView;
    }

    @FXML
    public void initialize() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();

        algebraButton.setOnAction(this::handleButtonClick);
        anglesButton.setOnAction(this::handleButtonClick);
        geometryButton.setOnAction(this::handleButtonClick);
        logicButton.setOnAction(this::handleButtonClick);
        probabilityButton.setOnAction(this::handleButtonClick);
        trigoButton.setOnAction(this::handleButtonClick);
    }

    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        selectedCategory = clickedButton.getText();

        requestQuestionFromServer(selectedCategory);
    }

    public static String getSelectedCategory() {
        return selectedCategory;
    }

    private void requestQuestionFromServer(String category) {
        new Thread(() -> {
            try {
                QuestionModel question = clientConnection.getQuestion(category);

                if (question != null) {
                    System.out.println("Received Question: " + question.getQuestionText());
                    updateUI(() -> categoryView.switchScene("/views/gameplay.fxml", "Question"));
                } else {
                    System.out.println("No questions found for category: " + category);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error fetching question: " + e.getMessage());
            }
        }).start();
    }

    private void updateUI(Runnable action) {
        javafx.application.Platform.runLater(action);
    }
}
