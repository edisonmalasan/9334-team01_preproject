package Server;

import Server.model.QuestionBankModel;
import Server.model.XMLStorageModel;

import java.io.IOException;

public class ServerMain {

    private static final int PORT_NUMBER = 5000;

    public static void main(String[] args) throws IOException {
        QuestionBankModel questionBank = new QuestionBankModel();
        XMLStorageModel.loadQuestionFromXML("data/question.xml", questionBank);

        System.out.println("QuestionBank loaded: " + questionBank.getAllQuestions().size());

        try {
            ServerHandler server = new ServerHandler(PORT_NUMBER, questionBank);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
