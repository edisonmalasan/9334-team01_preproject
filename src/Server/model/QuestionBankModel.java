package Server.model;

import common.model.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionBankModel {
    private List<QuestionModel> questions;

    public QuestionBankModel() {
        this.questions = XMLStorageModel.loadQuestionsFromXML("data/questions.xml");
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }
}
