package Test;

import Client.connection.ClientConnection;
import Client.controller.GameController;
import Server.model.QuestionBankModel;
import exception.ConnectionException;
import exception.InvalidRequestException;

public class ClientServerTest {

    public static void main(String[] args) throws ConnectionException {
        System.out.println("Starting client tests...");

        // client connection
        ClientConnection clientConnection;
        try {
            // Singleton connection
            clientConnection = ClientConnection.getInstance();

            if (!clientConnection.isConnected()) {
                System.out.println("Failed to connect to the server. Exiting tests.");
                return;
            }


            // Initialize game components
            QuestionBankModel questionBank = new QuestionBankModel();
            GameController gameController = new GameController(questionBank);

            // Run the game test
            gameController.startGame();
//        // test
//        //testFetchQuestionsByCategory(clientConnection);
//
//        // test
//        //testAddScoreToLeaderboard(clientConnection);
//
//        // test
//        //testFetchLeaderboard(clientConnection);
//
            clientConnection.close();
            System.out.println("Client tests completed.");
        } catch (ConnectionException e) {
            System.out.println("Error: Unable to connect to the server.");
            e.printStackTrace();
        }
    }

    private static void testFetchQuestionsByCategory(ClientConnection clientConnection) throws InvalidRequestException, ConnectionException {
        System.out.println("\nTesting fetch questions by category...");

        // fetching questions for a category
        String category = "Algebra";
        String request = "GET_QUESTION:" + category;
        String response = String.valueOf(clientConnection.sendRequest(request));

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to fetch questions for category: " + category);
        }
    }

    private static void testAddScoreToLeaderboard(ClientConnection clientConnection) throws InvalidRequestException, ConnectionException {
        System.out.println("\nTesting adding a score to the leaderboard...");

        //  adding a score
        String playerName = "TestPlayer";
        int score = 100;
        String request = "ADD_SCORE:" + playerName + ":" + score;
        String response = String.valueOf(clientConnection.sendRequest(request));

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to add score for player: " + playerName);
        }
    }

    private static void testFetchLeaderboard(ClientConnection clientConnection) throws InvalidRequestException, ConnectionException {
        System.out.println("\nTesting fetch leaderboard...");

        // fetching the leaderboard
        String request = "GET_LEADERBOARD";
        String response = String.valueOf(clientConnection.sendRequest(request));

        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("Failed to fetch the leaderboard.");
        }
    }
}