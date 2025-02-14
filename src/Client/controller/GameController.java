package Client.controller;

import Client.connection.ClientConnection;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;
import exception.ConnectionException;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GameController {
    public QuestionBankModel questionBank;
    public ClientConnection clientConnection;
    private static final int INITIAL_TIME = 180; // 3 minutes in seconds
    private int remainingTime;
    private final Random random = new Random();

    public GameController(QuestionBankModel questionBank) throws ConnectionException {
        this.questionBank = questionBank;
        this.clientConnection = ClientConnection.getInstance();
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

        // Debugging: Print if questions are available
        if (questions.isEmpty()) {
            System.out.println("No questions found in the question bank!");
        } else {
            System.out.println("Questions loaded from question bank:");
            for (QuestionModel q : questions) {
                System.out.println(" - " + q.getCategory() + ": " + q.getQuestionText());
            }
        }
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
        remainingTime = INITIAL_TIME;
        Set<Integer> qteTriggers = generateQTETriggers(questions.size());

        int totalScore = 0;
        for (int i = 0; i < questions.size() && remainingTime > 0; i++) {
            QuestionModel question = questions.get(i);
            System.out.println("\n" + question.getQuestionText());
            List<String> choices = question.getChoices();

            for (int j = 0; j < choices.size(); j++) {
                System.out.println((j + 1) + ". " + choices.get(j));
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
                    remainingTime -= 10; // Wrong answer penalty
                }
            } else {
                System.out.println("Invalid choice. Skipping question...");
            }

            // **Trigger Quick Time Event at random moments**
            if (qteTriggers.contains(i)) {
                boolean success = triggerQuickTimeEvent(scanner);
                if (!success) {
                    int penalty = determineQTEPenalty();
                    remainingTime -= penalty;
                    System.out.println("QTE failed! Time penalty: -" + penalty + " seconds. Remaining Time: " + remainingTime + " seconds.");
                }
            }

            if (remainingTime <= 0) {
                System.out.println("Time's up! Game over.");
                break;
            }
        }
        System.out.println("Total Score: " + totalScore);
    }

    /**
     * Generates random moments in the quiz where Quick Time Events will trigger.
     * Ensures a reasonable number of events (1-3) depending on the number of questions.
     */
    private Set<Integer> generateQTETriggers(int totalQuestions) {
        Set<Integer> qteTriggers = new HashSet<>();
        int qteCount = Math.min(3, totalQuestions); // Max 3 QTEs per game

        while (qteTriggers.size() < qteCount) {
            qteTriggers.add(random.nextInt(totalQuestions));
        }
        return qteTriggers;
    }

    /**
     * Simulates a quick time event where the player must react quickly.
     */
    private boolean triggerQuickTimeEvent(Scanner scanner) {
        System.out.println("\n QUICK TIME EVENT! ");
        System.out.println("Type 'DEFUSE' within 3 seconds to avoid penalty!");

        long startTime = System.currentTimeMillis();
        String response = scanner.nextLine();
        long reactionTime = (System.currentTimeMillis() - startTime) / 1000; // Convert to seconds

        return response.equalsIgnoreCase("DEFUSE") && reactionTime <= 3;
    }

    /**
     * Determines how much time to deduct when a QTE is failed.
     * The penalty increases as the game progresses.
     */
    private int determineQTEPenalty() {
        if (remainingTime > 90) return 30;  // 30-second penalty if time > 90s
        if (remainingTime > 45) return 20;  // 20-second penalty if time > 45s
        return 15;                          // 15-second penalty otherwise
    }
}
