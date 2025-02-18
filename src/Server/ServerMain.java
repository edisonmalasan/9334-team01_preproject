package Server;

import Server.controller.AdminViewController;
import Server.view.AdminView;
import common.AnsiFormatter;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardControllerServer;
import java.util.logging.Logger;

/**
 * Runs the server and admin view, loads the questions and leaderboard
 */
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
            AdminView classicView = new AdminView("data/classic_leaderboard.xml","Classic Leaderboards");
            AdminViewController classicController = new AdminViewController(classicView);
            classicController.loadXML();
            AdminView endlessView = new AdminView("data/endless_leaderboard.xml","Endless Leaderboards");
            AdminViewController endlessController = new AdminViewController(endlessView);
            endlessController.loadXML();
            server.start();
        } catch (Exception e) {
            logger.severe("Server Main: ❌ Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
