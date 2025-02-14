package Server.handler;

import Client.model.LeaderboardEntryModelClient;
import Client.model.PlayerModel;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardControllerServer;
import common.Protocol;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private ObjectInputStream objectInputStream;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private QuestionBankModel questionBank;
    private LeaderboardControllerServer leaderboardControllerServer;

    public ClientHandler(Socket clientSocket, QuestionBankModel questionBank, LeaderboardControllerServer leaderboardControllerServer) {
        this.clientSocket = clientSocket;
        this.questionBank = questionBank;
        this.leaderboardControllerServer = leaderboardControllerServer;

        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                String leaderboard = leaderboardControllerServer.getLeaderboard();
                return new Response(true, "Leaderboard retrieved successfully.", leaderboard);
            } else if (request.startsWith(Protocol.ADD_SCORE)) {
                String[] parts = request.split(":");
                if (parts.length < 3) {
                    return new Response(false, "Invalid ADD_SCORE request format.", null);
                }

                String playerName = parts[1].trim();
                int score;
                try {
                    score = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    return new Response(false, "Invalid score format.", null);
                }

                PlayerModel player = new PlayerModel(playerName, score);
                LeaderboardControllerServer.addScore(playerName, score);

                return new Response(true, "Score added and leaderboard updated.", null);
            } else if (request.equals(Protocol.UPDATE_LEADERBOARD)) {
                return handleLeaderboardUpdate();

            } else {
                return new Response(false, "Invalid request.", null);
            }
        } catch (Exception e) {
            return new Response(false, "Error processing request: " + e.getMessage(), null);
        }
    }

    private Response handleLeaderboardUpdate() {
        try {
            PlayerModel player = (PlayerModel) objectInputStream.readObject();

            if (player == null) {
                return new Response(false, "Received null player data.", null);
            }

            LeaderboardEntryModelClient entry = new LeaderboardEntryModelClient(player.getName(), player.getScore());

            leaderboardControllerServer.addScore(entry);

            return new Response(true, "Leaderboard updated successfully.", null);
        } catch (IOException | ClassNotFoundException e) {
            return new Response(false, "Error updating leaderboard: " + e.getMessage(), null);
        }
    }
}