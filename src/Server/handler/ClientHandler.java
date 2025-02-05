package Server.handler;

import Server.model.QuestionBankModel;
import common.Protocol;
import common.model.QuestionModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private QuestionBankModel questionBank;

    public ClientHandler(Socket clientSocket, QuestionBankModel questionBank) {
        this.clientSocket = clientSocket;
        this.questionBank = questionBank;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while ((request = input.readLine()) != null) {
                System.out.println("Received request from client: " + request);  // Log incoming request

                // Handle the request
                handleRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(String request) {
        if (request.startsWith(Protocol.GET_QUESTION)) {
            String category = request.split(":")[1].trim();
            System.out.println("Client requested questions for category: " + category);  // logger

            // fetch and send the question based on the category
            QuestionModel question = questionBank.getQuestions().stream()
                    .filter(q -> q.getCategory().equalsIgnoreCase(category))
                    .findFirst()
                    .orElse(null);

            if (question != null) {
                output.println("Question: " + question.getQuestionText());
                output.println("Choices: " + String.join(", ", question.getChoices()));
                System.out.println("Sending question to client: " + question.getQuestionText());  // logger
            } else {
                output.println("No questions found for the given category.");
                System.out.println("No question found for category: " + category);  // logger
            }
        } else {
            output.println("Invalid request.");
            System.out.println("Received an invalid request: " + request);  // logger
        }
    }
}
