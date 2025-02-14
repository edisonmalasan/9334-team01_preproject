package Test;

import Client.connection.ClientConnection;
import exception.ConnectionException;
import exception.InvalidRequestException;

public class ClientServerTest {

    public static void main(String[] args) throws ConnectionException {
        System.out.println("Starting client tests...");

//        // client connection
//        ClientConnection clientConnection = new ClientConnection();
//        if (clientConnection == null || !clientConnection.isConnected()) {
//            System.out.println("Failed to connect to the server. Exiting tests.");
//            return;
//        }
//
//
//        QuestionBankModel questionBank = new QuestionBankModel();
//        GameController gameController = new GameController(questionBank);
//        //testing
//        gameController.startGame();
//        // test
//        //testFetchQuestionsByCategory(clientConnection);
//
//        // test
//        //testAddScoreToLeaderboard(clientConnection);
//
//        // test
//        //testFetchLeaderboard(clientConnection);
//
//        clientConnection.close();
//        System.out.println("Client tests completed.");
    }
}