package Server.model;

import common.model.QuestionModel;
import java.util.*;

public class QuestionBankModel {
    // use synchronized list for thread
    private List<QuestionModel> questions;

    public QuestionBankModel() {
        this.questions = Collections.synchronizedList(XMLStorageModel.loadQuestionsFromXML("data/questions.xml"));
    }

    public List<QuestionModel> getQuestions() {
        // sync access to the questions list
        synchronized (questions) {
            return new ArrayList<>(questions);
        }
    }
}