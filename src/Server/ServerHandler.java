package Server;

import Client.connection.AnsiFormatter;
import Server.controller.LeaderboardControllerServer;
import Server.model.QuestionBankModel;
import Server.handler.ClientHandler;
import static common.Protocol.PORT_NUMBER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerHandler {
    private QuestionBankModel questionBank;
    private LeaderboardControllerServer leaderboard;
    private ServerSocket serverSocket;
    private static final Logger logger = Logger.getLogger(ServerHandler.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public ServerHandler(QuestionBankModel questionBank, LeaderboardControllerServer leaderboard) {
        this.questionBank = questionBank;
        this.leaderboard = leaderboard;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            logger.info("✅ Server started on port " + PORT_NUMBER);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("🔗 New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, questionBank, leaderboard);

                logger.info("🚀 Starting client thread for: " + clientSocket.getInetAddress());

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.severe("❌ Server error: " + e.getMessage());
        }
    }
}
