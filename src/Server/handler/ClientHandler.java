package Server.handler;

import Client.connection.AnsiFormatter;
import Client.model.PlayerModel;
import Server.controller.LeaderboardControllerServer;
import Server.controller.QuestionController;
import Server.model.LeaderboardEntryModelServer;
import Server.model.QuestionBankModel;
import Server.controller.XMLStorageController;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private LeaderboardControllerServer leaderboardControllerServer;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public ClientHandler(Socket clientSocket, QuestionBankModel questionBank, LeaderboardControllerServer leaderboardControllerServer) {
        this.clientSocket = clientSocket;
        this.leaderboardControllerServer = leaderboardControllerServer;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.severe("Error initializing streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            logger.info("New client connected: " + clientSocket.getInetAddress());

            while (!clientSocket.isClosed()) {
                Object request = objectInputStream.readObject();

                if (request instanceof String) {
                    String reqString = (String) request;
                    if (reqString.startsWith("GET_QUESTION:")) {
                        String category = reqString.split(":")[1].trim();
                        Response response = handleQuestionRequest(category);
                        sendResponse(response);
                    }
                } else if (request instanceof PlayerModel) {
                    logger.info("Player registration request received.");
                    Response response = handlePlayerRegistration((PlayerModel) request);
                    sendResponse(response);
                }
            }
        } catch (EOFException e) {
            logger.info("Client disconnected.");
        } catch (SocketException e) {
            logger.warning("Client forcibly closed the connection.");
        } catch (Exception e) {
            logger.severe("SERVER ERROR: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.info("Sending response to client: " + response);

        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.severe("Failed to send response: " + e.getMessage());
            throw e;
        }
    }

    private Response handlePlayerRegistration(PlayerModel player) {
        try {
            if (player == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }

            String usernameLower = player.getName().toLowerCase();
            logger.info("Registering player: " + usernameLower);

            List<LeaderboardEntryModelServer> leaderboard = XMLStorageController.loadLeaderboardFromXML("data/leaderboard.xml");

            boolean exists = leaderboard.stream()
                    .anyMatch(entry -> entry.getPlayerName().equalsIgnoreCase(usernameLower));

            if (exists) {
                logger.warning("Username already exists.");
                return new Response(false, "Username already taken!", null);
            }

            leaderboard.add(new LeaderboardEntryModelServer(usernameLower, 0));
            XMLStorageController.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);

            logger.info("Player registered successfully.");
            return new Response(true, "Player registered successfully.", null);
        } catch (Exception e) {
            logger.severe("Error registering player: " + e.getMessage());
            return new Response(false, "Error registering player: " + e.getMessage(), null);
        }
    }

    private Response handleQuestionRequest(String category) {
        QuestionController questionController = new QuestionController();
        List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

        if (questions.isEmpty()) {
            logger.warning("No questions found for category: " + category);
            return new Response(false, "No questions found for category: " + category, null);
        }

        QuestionModel question = questions.get(0);
        logger.info("Question retrieved successfully for category: " + category);
        return new Response(true, "Question retrieved successfully.", question);
    }

    private void closeConnection() {
        try {
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            logger.info("Connection closed.");
        } catch (IOException e) {
            logger.severe("Error closing connection: " + e.getMessage());
        }
    }
}
