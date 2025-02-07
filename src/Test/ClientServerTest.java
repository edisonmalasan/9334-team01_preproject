package Test;

import Client.connection.ClientConnection;
import Client.controller.CategoryController;
import Client.controller.LeaderboardController;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.util.List;

public class ClientServerTest {

    public static void main(String[] args) {
        System.out.println("Starting client tests...");

        // client connection
        ClientConnection clientConnection = new ClientConnection();
        if (clientConnection == null || !clientConnection.isConnected()) {
            System.out.println("Failed to connect to the server. Exiting tests.");
            return;
        }

        // test
        testFetchQuestionsByCategory(clientConnection);

        // test
        testAddScoreToLeaderboard(clientConnection);

        // test
        testFetchLeaderboard(clientConnection);

        clientConnection.close();
        System.out.println("Client tests completed.");
    }

    private static void testFetchQuestionsByCategory(ClientConnection clientConnection) {
        System.out.println("\nTesting fetch questions by category...");

        // fetching questions for a category
        String category = "Algebra";
        String request = "GET_QUESTION:" + category;
        String response = clientConnection.sendRequest(request);

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to fetch questions for category: " + category);
        }
    }

    private static void testAddScoreToLeaderboard(ClientConnection clientConnection) {
        System.out.println("\nTesting adding a score to the leaderboard...");

        //  adding a score
        String playerName = "TestPlayer";
        int score = 100;
        String request = "ADD_SCORE:" + playerName + ":" + score;
        String response = clientConnection.sendRequest(request);

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to add score for player: " + playerName);
        }
    }

    private static void testFetchLeaderboard(ClientConnection clientConnection) {
        System.out.println("\nTesting fetch leaderboard...");

        // fetching the leaderboard
        String request = "GET_LEADERBOARD";
        String response = clientConnection.sendRequest(request);

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to fetch the leaderboard.");
        }
    }
}