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
            switchToCategorySelection();
        });

        endlessButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Endless button clicked.");
            switchToEndlessMode();
        });

        returnButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Return button clicked.");
            returnToMainMenu();
        });
    }

    private void switchToCategorySelection() {
        try {
            logger.info("\nModeController: Switching to Category Selection.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/category_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) classicButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Select a Category");
            stage.setResizable(false);
            stage.show();

            logger.info("\nModeController: Successfully switched to Category Selection.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "\nModeController: Failed to load Category Menu", e);
        }
    }

    private void switchToEndlessMode() {
        try {
            logger.info("\nModeController: Switching to Endless Mode.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/category_menu.fxml"));
            Parent root = loader.load();

            CategoryController categoryController = loader.getController();
            categoryController.setEndlessMode(true); // Set Endless Mode flag

            Stage stage = (Stage) endlessButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Select a Category");
            stage.setResizable(false);
            stage.show();

            logger.info("\nModeController: Successfully switched to Category Selection for Endless Mode.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "\nModeController: Failed to load Category Menu for Endless Mode", e);
        }
    }

    public void returnToMainMenu() {
        try {
            logger.info("\nModeController: Returning to Main Menu.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("\nModeController: Successfully returned to Main Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "\nModeController: Failed to return to Main Menu", e);
        }
    }
}
