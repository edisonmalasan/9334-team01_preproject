package Client.controller;

import Client.connection.ClientConnection;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryController {
    private final ClientConnection clientConnection;
    private final QuestionBankModel questionBank;
    private static String category;

    @FXML
    public AnchorPane categoryMenu;

    @FXML
    private Button algebraButton, anglesButton, geometryButton, logicButton, probabilityButton, trigoButton;

    @FXML
    private ImageView categoryLabel;

    public CategoryController(ClientConnection clientConnection, QuestionBankModel questionBank) {
        this.clientConnection = clientConnection;
        this.questionBank = questionBank;
    }

    @FXML
    public void initialize() {
        algebraButton.setOnAction(this::handleButtonClick);
        anglesButton.setOnAction(this::handleButtonClick);
        geometryButton.setOnAction(this::handleButtonClick);
        logicButton.setOnAction(this::handleButtonClick);
        probabilityButton.setOnAction(this::handleButtonClick);
        trigoButton.setOnAction(this::handleButtonClick);
    }

    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        category = clickedButton.getText(); // Get category from button text

        handleCategorySelection(category);
    }

    public static String getCategory() {
        return category;
    }

    private void handleCategorySelection(String category) {
        List<QuestionModel> filteredQuestions = filterQuestionsByCategory(category);

        if (!filteredQuestions.isEmpty()) {
            QuestionModel question = filteredQuestions.get(0);

            // Assuming GameView is a JavaFX View, ensure correct scene handling
//            new GameView(clientConnection, question);
        } else {
            System.out.println("No questions found for the selected category: " + category);
        }
    }

    private List<QuestionModel> filterQuestionsByCategory(String category) {
        return questionBank.getQuestions().stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
