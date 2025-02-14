package Server.handler;

import Client.model.PlayerModel;
import Server.controller.LeaderboardControllerServer;
import Server.controller.QuestionController;
import Server.model.QuestionBankModel;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

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
            while (true) {
                Object request = objectInputStream.readObject();

                if (request instanceof String) {
                    String reqString = (String) request;

                    if (reqString.startsWith("GET_QUESTION:")) {
                        String category = reqString.split(":")[1].trim();
                        Response response = handleQuestionRequest(category);
                        objectOutputStream.writeObject(response);
                        objectOutputStream.flush();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

}
