package Client.controller;

import Client.view.ViewManager;
import Server.handler.ClientHandler;
import common.AnsiFormatter;
import common.LoggerSetup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class ModeController {
//    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");
    private static final Logger logger = Logger.getLogger(ModeController.class.getName());

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

            ViewManager.goTo(actionEvent, ViewManager.CATEGORY_MENU, "Select a Category", loader -> {
                CategoryController categoryController = loader.getController();
                categoryController.setEndlessMode(true); // set endless mode flag
            });

            logger.info("\nModeController: Successfully switched to Category Selection for Endless Mode.");
    }
}
