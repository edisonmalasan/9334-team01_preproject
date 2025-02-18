package Server.handler;

import common.AnsiFormatter;
import Client.model.PlayerModel;
import Server.controller.LeaderboardControllerServer;
import Server.controller.QuestionController;
import Server.model.LeaderboardEntryModelServer;
import Server.model.QuestionBankModel;
import Server.controller.XMLStorageController;
import common.LoggerSetup;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles client requests
 */
public class ClientHandler implements Runnable {
    private String fileName;
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private LeaderboardControllerServer leaderboardControllerServer;
//    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Server/Log/server.log");
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
                    } else if (reqString.equals("GET_LEADERBOARD_CLASSIC")) {
                        List<LeaderboardEntryModelServer> classicLeaderboard = LeaderboardControllerServer.getClassicLeaderboard();
                        Response response = handleLeaderboardUpdate(classicLeaderboard,"classic");
                        sendResponse(response);
                    } else if (reqString.equals("GET_LEADERBOARD_ENDLESS")) {
                        List<LeaderboardEntryModelServer> endlessLeaderboard = LeaderboardControllerServer.getEndlessLeaderboard();
                        Response response = handleLeaderboardUpdate(endlessLeaderboard,"endless");
                        sendResponse(response);
                    }
                } else if (request instanceof PlayerModel) {
                    logger.info("Player score update request received.");
                    Response response = handlePlayerScoreUpdate((PlayerModel) request);
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

    private Response handleLeaderboardUpdate(List<?> list, String xmlFile) {
        try {
            if (list == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }

            if (xmlFile.equals("classic")) {
                fileName = "data/classic_leaderboard.xml";
            } else {
                fileName = "data/endless_leaderboard.xml";
            }
            List<LeaderboardEntryModelServer> leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);
            logger.info("Returning leaderboard data.");
            return new Response(true, "Leaderboard displayed successfully.", leaderboard);
        } catch (Exception e) {
            logger.severe("Error retrieving leaderboard: " + e.getMessage());
            return new Response(false, "Error retrieving leaderboard: " + e.getMessage(), null);
        }
    }

    private Response handlePlayerScoreUpdate(PlayerModel player) {
        try {
            if (player == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }

            String usernameLower = player.getName().toLowerCase();
            int newScore = player.getScore();
            logger.info("Updating player score: " + usernameLower + " with score: " + newScore);

            fileName = "data/classic_leaderboard.xml";
            List<LeaderboardEntryModelServer> leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);

            if (player.getName().endsWith("  ")) {
                player.setName(player.getName().trim());
                fileName = "data/endless_leaderboard.xml";
                leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);
            }

            boolean found = false;
            for (LeaderboardEntryModelServer entry : leaderboard) {
                if (entry.getPlayerName().equalsIgnoreCase(usernameLower)) {
                    // compare the new score with the existing score
                    if (newScore > entry.getScore()) {
                        entry.setScore(newScore); // update the score only if the new score is higher
                        logger.info("Updated score for player: " + usernameLower + " to: " + newScore);
                    } else {
                        logger.info("New score is not higher. Keeping the existing score: " + entry.getScore());
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                leaderboard.add(new LeaderboardEntryModelServer(usernameLower, newScore));
                logger.info("Added new player to leaderboard: " + usernameLower + " with score: " + newScore);
            }

            XMLStorageController.saveLeaderboardToXML(fileName, leaderboard);

            logger.info("Player score updated successfully.");
            return new Response(true, "Player score updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating player score: " + e.getMessage());
            return new Response(false, "Error updating player score: " + e.getMessage(), null);
        }
    }

    private Response handleQuestionRequest(String category) {
        logger.info("Server received question request for category: " + category);

        QuestionController questionController = new QuestionController();
        List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

        if (questions.isEmpty()) {
            logger.warning("No questions found for category: " + category);
            return new Response(false, "No questions found for category: " + category, null);
        }

        logger.info("Question retrieved successfully for category: " + category);
        return new Response(true, "Question retrieved successfully.", questions);
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