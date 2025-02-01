package Server;

import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private QuestionBankModel questionBank;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, QuestionBankModel questionBank) {
        this.clientSocket = socket;
        this.questionBank = questionBank;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(),true);

            out.println("Welcome to the Bomb Defuse Game Server!");

            String difficulty;
            while ((difficulty = in.readLine()) != null) {
                QuestionModel question = questionBank.getRandomQuestion(difficulty);
                if (question != null) {
                    out.println("Question: " + question.getQuestionText());
                    out.println("Difficulty: " + question.getDifficulty());
                } else {
                    out.println("No question available for difficulty: " + difficulty);
                }

            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
