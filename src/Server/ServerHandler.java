package Server;

import common.AnsiFormatter;
import Server.controller.LeaderboardControllerServer;
import Server.model.QuestionBankModel;
import Server.handler.ClientHandler;
import common.LoggerSetup;

import static common.Protocol.PORT_NUMBER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Represents a server
 */
public class ServerHandler {
    private QuestionBankModel questionBank;
    private LeaderboardControllerServer leaderboard;
    private ServerSocket serverSocket;
//    private static final Logger logger = Logger.getLogger(ServerHandler.class.getName());
    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Server/Log/server.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public ServerHandler(QuestionBankModel questionBank, LeaderboardControllerServer leaderboard) {
        this.questionBank = questionBank;
        this.leaderboard = leaderboard;
    }

    /**
     * Starts the server
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            logger.info("Server Handler: ✅ Server started on port " + PORT_NUMBER);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Server Handler: 🔗 New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, questionBank, leaderboard);

                logger.info("Server Handler: 🚀 Starting client thread for: " + clientSocket.getInetAddress());

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.severe("Server Handler: ❌ Server error: " + e.getMessage());
        }
    }
}
