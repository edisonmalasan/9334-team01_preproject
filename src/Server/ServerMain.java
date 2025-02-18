package Server;

import Server.controller.AdminViewClassicController;
import Server.view.AdminViewClassic;
import common.AnsiFormatter;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardControllerServer;

import java.util.logging.Logger;

public class ServerMain {
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public static void main(String[] args) {
        QuestionBankModel questionBank = new QuestionBankModel();
        LeaderboardControllerServer leaderboardControllerServer = new LeaderboardControllerServer();

        logger.info("Server Main: 📖 QuestionBank loaded: " + questionBank.getQuestions().size() + " questions.");
        logger.info("Server Main: 🏆 Leaderboard controller initialized.");

        try {
            ServerHandler server = new ServerHandler(questionBank, leaderboardControllerServer);
            AdminViewClassic view = new AdminViewClassic("data/classic_leaderboard.xml");
            AdminViewClassicController controller = new AdminViewClassicController(view);
            controller.loadXML();
            server.start();
        } catch (Exception e) {
            logger.severe("Server Main: ❌ Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
