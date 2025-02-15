package Server.controller;

import Server.model.XMLStorageModel;
import common.model.QuestionModel;

import java.util.*;
import java.util.stream.Collectors;

public class QuestionController {
    private static final String QUESTIONS_FILE = "data/questions.xml";

    public List<QuestionModel> getQuestionsByCategory(String category) {
        List<QuestionModel> allQuestions = XMLStorageModel.loadQuestionsFromXML(QUESTIONS_FILE);

        return allQuestions.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
