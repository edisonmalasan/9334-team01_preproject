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
}