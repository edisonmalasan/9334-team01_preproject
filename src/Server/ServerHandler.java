package Server;

import Server.model.QuestionBankModel;
import Server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {
    private QuestionBankModel questionBank;
    private ServerSocket serverSocket;
    private final int PORT_NUMBER;

    public ServerHandler(int PORT_NUMBER, QuestionBankModel questionBank) {
        this.PORT_NUMBER = PORT_NUMBER;
        this.questionBank = questionBank;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Server started on port: " + PORT_NUMBER);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new ClientHandler(clientSocket, questionBank).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
