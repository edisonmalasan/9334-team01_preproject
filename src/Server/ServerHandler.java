package Server;

import Server.controller.LeaderboardController;
import Server.model.LeaderboardModel;
import Server.model.QuestionBankModel;
import Server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {
    private QuestionBankModel questionBank;
    private LeaderboardController leaderboard;
    private ServerSocket serverSocket;
    private final int PORT_NUMBER;

    public ServerHandler(int PORT_NUMBER, QuestionBankModel questionBank, LeaderboardController leaderboard) {
        this.PORT_NUMBER = PORT_NUMBER;
        this.questionBank = questionBank;
        this.leaderboard = leaderboard;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("Server started on port " + PORT_NUMBER);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket, questionBank, leaderboard);

            System.out.println("Client: " + clientSocket.getInetAddress());

            new Thread(clientHandler).start();
        }
    }
}
