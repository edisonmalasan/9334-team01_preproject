package Server.handler;

import Client.model.PlayerModel;
import Server.controller.LeaderboardControllerServer;
import Server.controller.QuestionController;
import Server.model.LeaderboardEntryModelServer;
import Server.model.QuestionBankModel;
import Server.model.XMLStorageModel;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private LeaderboardControllerServer leaderboardControllerServer;

    public ClientHandler(Socket clientSocket, QuestionBankModel questionBank, LeaderboardControllerServer leaderboardControllerServer) {
        this.clientSocket = clientSocket;
        this.leaderboardControllerServer = leaderboardControllerServer;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("New client connected: " + clientSocket.getInetAddress());

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
                    System.out.println("DEBUG: Player registration request received.");
                    Response response = handlePlayerRegistration((PlayerModel) request);
                    sendResponse(response);
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (SocketException e) {
            System.out.println("Client forcibly closed the connection.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void sendResponse(Response response) throws IOException {
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
    }



    private Response handlePlayerRegistration(PlayerModel player) {
        try {
            System.out.println("DEBUG: Registering player: " + player.getName());

            if (player == null) {
                return new Response(false, "Received null player data.", null);
            }

            String usernameLower = player.getName().toLowerCase();

            List<LeaderboardEntryModelServer> leaderboard = XMLStorageModel.loadLeaderboardFromXML("data/leaderboard.xml");

            boolean exists = leaderboard.stream()
                    .anyMatch(entry -> entry.getPlayerName().equalsIgnoreCase(usernameLower));

            if (exists) {
                System.out.println("DEBUG: Username already exists.");
                return new Response(false, "Username already taken!", null);
            }

            leaderboard.add(new LeaderboardEntryModelServer(usernameLower, 0));
            XMLStorageModel.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);

            System.out.println("DEBUG: Player registered successfully.");
            return new Response(true, "Player registered successfully.", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Error registering player: " + e.getMessage(), null);
        }
    }



    private Response handleQuestionRequest(String category) {
        QuestionController questionController = new QuestionController();
        List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

        if (questions.isEmpty()) {
            return new Response(false, "No questions found for category: " + category, null);
        }

        QuestionModel question = questions.get(0);

        return new Response(true, "Question retrieved successfully.", question);
    }

    private void closeConnection() {
        try {
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
