package Client.controller;

import Client.connection.ClientConnection;
import common.model.QuestionModel;
import exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;

public class CategoryController {
    private ClientConnection clientConnection;
    private static String selectedCategory;

    @FXML
    private Button algebraButton, anglesButton, geometryButton, logicButton, probabilityButton, trigoButton;

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
        try {
            QuestionModel question = clientConnection.getQuestion(category);

            if (question != null) {
                System.out.println("Received Question: " + question.getQuestionText());
                //
            } else {
                System.out.println("No questions found for category: " + category);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error fetching question: " + e.getMessage());
        }
    }
}
