package Server;

import Server.model.QuestionBankModel;
import Server.controller.LeaderboardController;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        QuestionBankModel questionBank = new QuestionBankModel();
        LeaderboardController leaderboardController = new LeaderboardController();

        System.out.println("QuestionBank loaded: " + questionBank.getQuestions().size());
        System.out.println("Leaderboard controller initialized.");

        try {
            // pass questionBank  & leaderboardController
            ServerHandler server = new ServerHandler(questionBank, leaderboardController);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
