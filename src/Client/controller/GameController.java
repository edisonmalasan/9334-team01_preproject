package Client.controller;

import Server.handler.ClientHandler;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;

public class GameController {
    private QuestionBankModel questionBank;
    private ClientHandler clientHandler;

    public GameController(ClientHandler clientHandler, QuestionBankModel questionBank) {
        this.questionBank = questionBank;
        this.clientHandler = clientHandler;
    }

    public void handleCategorySelection() {
        List<QuestionModel> questions = questionBank.getQuestions();
    }
}
