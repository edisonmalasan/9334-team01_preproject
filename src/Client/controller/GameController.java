package Client.controller;

import Client.connection.ClientConnection;
import Client.model.GameModel;
import Client.view.GameView;
import Server.handler.ClientHandler;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameController {
    /**
     *
     */
    private GameView gameView;
    /**
     *
     */
    private GameModel gameModel;
    private QuestionBankModel questionBank;
    //switched from ClientHandler to ClientConnection for testing
    private ClientConnection clientConnection;

    public GameController(ClientConnection clientConnection, QuestionBankModel questionBank) {
        this.questionBank = questionBank;
        this.clientConnection = clientConnection;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAvailable Categories:");
            List<String> categories = getCategories();
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }

            System.out.print("\nSelect a category (enter number) or 0 to exit: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("Exiting game...");
                break;
            }

            if (choice > 0 && choice <= categories.size()) {
                String selectedCategory = categories.get(choice - 1);
                playCategory(selectedCategory, scanner);
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    private List<String> getCategories() {
        List<QuestionModel> questions = questionBank.getQuestions();
        return questions.stream()
                .map(QuestionModel::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    private void playCategory(String category, Scanner scanner) {
        List<QuestionModel> questions = questionBank.getQuestions().stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        System.out.println("\nStarting " + category + " Quiz!");

        int totalScore = 0;
        for (QuestionModel question : questions) {
            System.out.println("\n" + question.getQuestionText());
            List<String> choices = question.getChoices();

            for (int i = 0; i < choices.size(); i++) {
                System.out.println((i + 1) + ". " + choices.get(i));
            }

            System.out.print("Enter your answer (1-" + choices.size() + "): ");
            int answerIndex = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            if (answerIndex >= 0 && answerIndex < choices.size()) {
                String selectedAnswer = choices.get(answerIndex);
                if (selectedAnswer.equals(question.getCorrectAnswer())) {
                    System.out.println("Correct!");
                    totalScore += question.getScore();
                } else {
                    System.out.println("Wrong! The correct answer is: " + question.getCorrectAnswer());
                }
            } else {
                System.out.println("Invalid choice. Skipping question...");
            }
        }
        System.out.println("Total Score: " + totalScore);
    }
}

