package Server.controller;

import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionController {
    private QuestionBankModel questionBank;

    public QuestionController() {
        this.questionBank = new QuestionBankModel();
    }

    // get all questions from xml
    public List<QuestionModel> getAllQuestions() {
        return questionBank.getQuestions();
    }

    public List<QuestionModel> getQuestionByCategory(String category) {
        return questionBank.getQuestions().stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
