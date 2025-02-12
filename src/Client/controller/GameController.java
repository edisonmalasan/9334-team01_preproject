package Client.controller;

import Client.connection.ClientConnection;
import Client.view.GameView;
import Server.handler.ClientHandler;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;

public class GameController {
    private static GameView view;
    private QuestionBankModel questionBank;
    private ClientHandler clientHandler;
    private ClientConnection clientConnection;

    public GameController(ClientHandler clientHandler, QuestionBankModel questionBank) {
        this.questionBank = questionBank;
        this.clientHandler = clientHandler;
        view = new GameView(clientConnection);
    }
    public void handleCategorySelection() {
        List<QuestionModel> questions = questionBank.getQuestions();
    }

    private static void fetchQuestions(ClientConnection clientConnection) {
        String request = "GET_QUESTION:" + CategoryController.getCategory();
        String response = clientConnection.sendRequest(request);
        view.questionText.setText(response);
    }

    private static void fetchChoices(ClientConnection clientConnection) {
        String request = "GET_CHOICES:" + CategoryController.getCategory();
        String response[] = clientConnection.sendRequest(request).split(",");
        view.option1.setText(response[0]);
        view.option2.setText(response[1]);
        view.option3.setText(response[2]);
        view.option4.setText(response[3]);
    }

}
