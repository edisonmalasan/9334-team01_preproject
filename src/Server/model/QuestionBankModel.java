package Server.model;

import java.util.*;
import common.model.QuestionModel;

public class QuestionBankModel {
    private List<QuestionModel> questions;

    public QuestionBankModel() {
        questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        // TODO: load questions from XMLStorageModel.java
        questions.add(new QuestionModel("easy", "2 + 2 = ?", "4"));
        questions.add(new QuestionModel("medium", "What is 5 * 6?", "30"));
        questions.add(new QuestionModel("hard", "Solve: (8+2)/2 = ?", "5"));
    }

    public QuestionModel getRandomQuestion(String difficulty) {
        List<QuestionModel> filtered = new ArrayList<>();
        for (QuestionModel q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filtered.add(q);
            }
        }
        return filtered.isEmpty() ? null : filtered.get(new Random().nextInt(filtered.size()));
    }
}
