package Client.controller;

import Client.connection.ClientConnection;
import Client.view.CategoryView;
import Client.view.GameView;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryController {
    private ClientConnection clientConnection;
    private QuestionBankModel questionBank;
    private CategoryView view;
    private GameView gameView;
    public static String category;
    public CategoryController(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.questionBank = questionBank;
        this.view = new CategoryView();

        view.getCategory1().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category1){
                category = view.category1.getText();
                view.dispose();
                gameView = new GameView(clientConnection);

            }
        });

        view.getCategory2().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category2){
                category = view.category2.getText();
                view.dispose();
                gameView = new GameView(clientConnection);
            }
        });

        view.getCategory3().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category3){
                category = view.category3.getText();
                view.dispose();
                gameView = new GameView(clientConnection);
            }
        });

        view.getCategory4().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category4){
                category = view.category4.getText();
                view.dispose();
            }
        });

        view.getCategory5().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category5){
                category = view.category5.getText();
                view.dispose();
            }
        });

        view.getCategory6().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.category6){
                category = view.category6.getText();
                view.dispose();
            }
        });
    }

    public static String getCategory() {
        return category;
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
