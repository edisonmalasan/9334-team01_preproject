package Client.controller;

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
            switchToModeMenu();
        });

        leaderboardButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Leaderboard button clicked.");
            switchToLeaderboard();
        });

        quitButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Exiting application.");
            System.exit(0);
        });
    }

    private void switchToModeMenu() {
        try {
            logger.info("\nMainMenuController: Switching to Mode Menu.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mode_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("\nMainMenuController: Successfully switched to Mode Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "\nMainMenuController: Failed to load Mode Menu", e);
        }
    }

    private void switchToLeaderboard() {
        try {
            logger.info("\nMainMenuController: Switching to Leaderboard.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/leaderboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboards");
            stage.setResizable(false);
            stage.show();

            logger.info("\nMainMenuController: Successfully switched to Leaderboard.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "\nMainMenuController: Failed to load Leaderboard", e);
        }
    }
}