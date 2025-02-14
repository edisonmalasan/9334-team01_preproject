package Server;

import Server.model.QuestionBankModel;
import Server.controller.LeaderboardControllerServer;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        QuestionBankModel questionBank = new QuestionBankModel();
        LeaderboardControllerServer leaderboardControllerServer = new LeaderboardControllerServer();

        System.out.println("QuestionBank loaded: " + questionBank.getQuestions().size());
        System.out.println("Leaderboard controller initialized.");

        try {
            // pass questionBank  & leaderboardController
            ServerHandler server = new ServerHandler(questionBank, leaderboardControllerServer);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
