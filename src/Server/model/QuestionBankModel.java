package Server.model;

import common.model.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionBankModel {
    private List<QuestionModel> questions;


    public QuestionBankModel() {
        questions = new ArrayList<>();
    }

    public void add(QuestionModel question) {
        questions.add(question);
    }

    public List<QuestionModel> getAllQuestions() {
        return questions;
    }


    public QuestionModel getRandomQuestion(String difficulty) {
        List<QuestionModel> filtered = new ArrayList<>();
        for (QuestionModel q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filtered.add(q);
            }
        }
        return filtered.isEmpty() ? null : filtered.get((int) (Math.random() * filtered.size()));
    }
}
