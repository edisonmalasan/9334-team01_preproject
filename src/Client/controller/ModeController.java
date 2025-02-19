package Client.controller;

import Client.view.ViewManager;
import common.AnsiFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Logger;

/**
 * Manipulates mode view
 */
public class ModeController {
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
            CategoryController.isEndlessMode = false;
            ViewManager.goTo(actionEvent, ViewManager.CATEGORY_MENU, "Bomb Defusing Game");
        });

        endlessButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Endless button clicked.");

            CategoryController.isEndlessMode = true;
            ViewManager.goTo(actionEvent, ViewManager.CATEGORY_MENU, "Bomb Defusing Game");
        });

        returnButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Return button clicked.");
            ViewManager.goTo(actionEvent, ViewManager.MAIN_MENU, "Bomb Defusing Game");
        });
    }
}
