package Server;

import Server.model.QuestionBankModel;
import Server.model.LeaderboardModel;

import java.io.IOException;

public class ServerMain {

    private static final int PORT_NUMBER = 5000;

    public static void main(String[] args) throws IOException {
        QuestionBankModel questionBank = new QuestionBankModel();
        LeaderboardModel leaderboard = new LeaderboardModel();
        System.out.println("QuestionBank loaded: " + questionBank.getQuestions().size());
        System.out.println("LeaderBoard loaded: " + leaderboard.getLeaderboardEntries().size());

        try {
            ServerHandler server = new ServerHandler(PORT_NUMBER, questionBank);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
