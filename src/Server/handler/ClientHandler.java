package Server.handler;

import Client.model.PlayerModel;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardController;
import common.Protocol;
import common.Response;
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
                Response response = handleRequest(request);
                output.println(response.toString());
            }
        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    private Response handleRequest(String request) {
        try {
            if (request.startsWith(Protocol.GET_QUESTION)) {
                String category = request.split(":")[1].trim();
                QuestionModel question = questionBank.getQuestions().stream()
                        .filter(q -> q.getCategory().equalsIgnoreCase(category))
                        .findFirst()
                        .orElse(null);

                if (question != null) {
                    return new Response(true, "Question retrieved successfully.", question);
                } else {
                    return new Response(false, "No questions found for the given category.", null);
                }
            } else if (request.equals(Protocol.GET_LEADERBOARD)) {
                String leaderboard = leaderboardController.getLeaderboard();
                return new Response(true, "Leaderboard retrieved successfully.", leaderboard);
            } else if (request.equals(Protocol.ADD_SCORE)) {
                String[] parts = request.split(":");
                String playerName = parts[1].trim();
                int score = Integer.parseInt(parts[2].trim());
                leaderboardController.addScore(new PlayerModel(playerName, score));
                return new Response(true, "Score added successfully.", null);
            } else {
                return new Response(false, "Invalid request.", null);
            }
        } catch (Exception e) {
            return new Response(false, "Error processing request: " + e.getMessage(), null);
        }
    }
}