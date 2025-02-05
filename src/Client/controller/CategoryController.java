package Client.controller;

import Client.connection.ClientConnection;
import Client.view.GameView;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryController {
    private ClientConnection clientConnection;
    private QuestionBankModel questionBank;

    public CategoryController(ClientConnection clientConnection, QuestionBankModel questionBank) {
        this.clientConnection = clientConnection;
        this.questionBank = questionBank;
    }

    public void handleCategorySelection(String category) {
        List<QuestionModel> filteredQuestions = filterQuestionsByCategory(category);

        if (!filteredQuestions.isEmpty()) {
            QuestionModel question = filteredQuestions.get(0);

            new GameView(clientConnection, question);
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
