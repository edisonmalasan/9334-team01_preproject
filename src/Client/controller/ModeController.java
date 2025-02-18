package Client.controller;

import Client.view.ViewManager;
import common.AnsiFormatter;
import common.LoggerSetup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeController {
    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", "Client/client.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    public Button returnButton;

    @FXML
    private Button classicButton;

    @FXML
    private Button endlessButton;

    @FXML
    public void initialize() {
        classicButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Classic button clicked.");
            ViewManager.goTo(actionEvent, ViewManager.CATEGORY_MENU, "Bomb Defusing Game");
        });

        endlessButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Endless button clicked.");
            switchToEndlessMode(actionEvent);
        });

        returnButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Return button clicked.");
            ViewManager.goTo(actionEvent, ViewManager.MAIN_MENU, "Bomb Defusing Game");
        });
    }

    private void switchToEndlessMode(ActionEvent actionEvent) {

            logger.info("\nModeController: Switching to Endless Mode.");

            ViewManager.goTo(actionEvent, ViewManager.ENDLESS_GAME, "Endless Mode", loader -> {
                CategoryController categoryController = new CategoryController();
                categoryController.setEndlessMode(true); // set endless mode flag
            });

            logger.info("\nModeController: Successfully switched to Category Selection for Endless Mode.");
    }
}
