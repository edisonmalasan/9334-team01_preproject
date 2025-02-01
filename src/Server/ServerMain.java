package Server;

import Server.model.QuestionBankModel;
import Server.model.XMLStorageModel;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        QuestionBankModel questionBank = new QuestionBankModel();
        XMLStorageModel.loadQuestionFromXML("data/question.xml", questionBank);

        System.out.println("QuestionBank loaded: " + questionBank.getAllQuestions().size());

        int PORT_NUMBER = 5000;
        ServerHandler server = new ServerHandler(PORT_NUMBER, questionBank);
        server.start();
    }
}
