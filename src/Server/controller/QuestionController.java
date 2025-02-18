package Server.controller;

import common.model.QuestionModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for getting questions based on category
 */
public class QuestionController {
    private static final String QUESTIONS_FILE = "data/questions.xml";

    /**
     * Returns a list of questions for a certain category
     */
    public List<QuestionModel> getQuestionsByCategory(String category) {
        List<QuestionModel> allQuestions = XMLStorageController.loadQuestionsFromXML(QUESTIONS_FILE);

        return allQuestions.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
