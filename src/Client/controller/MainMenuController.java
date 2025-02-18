package Client.controller;

import Client.view.ViewManager;
import common.AnsiFormatter;
import common.LoggerSetup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", "Client/client.log");

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