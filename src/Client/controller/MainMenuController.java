package Client.controller;
/**
 * Controls main menu view window
 */
import Client.view.ViewManager;
import common.AnsiFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class MainMenuController {
    private static final Logger logger = Logger.getLogger(MainMenuController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button quitButton;

    @FXML
    public void initialize() {
        playButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Play button clicked.");
            ViewManager.goTo(actionEvent, ViewManager.MODE_MENU, "Bomb Defusing Game");
        });

        leaderboardButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Leaderboard button clicked.");
            ViewManager.goTo(actionEvent, ViewManager.LEADERBOARD, "Leaderboard");
        });

        quitButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Exiting application.");
            System.exit(0);
        });
    }
}