package Server.handler;

import Client.model.PlayerModel;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardController;
import common.Protocol;
import common.model.QuestionModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private QuestionBankModel questionBank;
    private LeaderboardController leaderboardController;

    public ClientHandler(Socket clientSocket, QuestionBankModel questionBank, LeaderboardController leaderboardController) {
        this.clientSocket = clientSocket;
        this.questionBank = questionBank;
        this.leaderboardController = leaderboardController;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while ((request = input.readLine()) != null) {
                System.out.println("Received request from client: " + request);
                handleRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(String request) {
        if (request.startsWith(Protocol.GET_QUESTION)) {
            String category = request.split(":")[1].trim();
            System.out.println("Client requested questions for category: " + category);

            QuestionModel question = questionBank.getQuestions().stream()
                    .filter(q -> q.getCategory().equalsIgnoreCase(category))
                    .findFirst()
                    .orElse(null);

            if (question != null) {
                output.println("Question: " + question.getQuestionText());
                System.out.println("Sending question to client: " + question.getQuestionText());
            } else {
                output.println("No questions found for the given category.");
                System.out.println("No question found for category: " + category);
            }
        } else if (request.startsWith(Protocol.GET_CHOICES)) {
            String category = request.split(":")[1].trim();
            System.out.println("Client requested choices for category: " + category);

            QuestionModel question = questionBank.getQuestions().stream()
                    .filter(q -> q.getCategory().equalsIgnoreCase(category))
                    .findFirst()
                    .orElse(null);

            if (question != null) {
                output.println("Choices: " + String.join(",", question.getChoices()));
                System.out.println("Sending choices to client: " + question.getChoices());
            } else {
                output.println("No choices found for the given category.");
                System.out.println("No choices found for category: " + category);
            }
        } else if (request.equals(Protocol.GET_LEADERBOARD)) {
            System.out.println("Client requested the leaderboard.");
            String leaderboard = LeaderboardController.getLeaderboard();
            output.println(leaderboard);
            System.out.println("Sending leaderboard to client.");

        } else if (request.startsWith(Protocol.ADD_SCORE)) {
            String[] parts = request.split(":");
            String playerName = parts[1].trim();
            int score = Integer.parseInt(parts[2].trim());
            PlayerModel player = new PlayerModel(playerName,score);

            System.out.println("Client requested to add score: " + playerName + " with score " + score);
            leaderboardController.addScore(player);
            output.println("Score added successfully!");
            System.out.println("Added score for player: " + playerName + " with score: " + score);
        } else {
            output.println("Invalid request.");
            System.out.println("Received an invalid request: " + request);
        }
    }
}