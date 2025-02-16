package Client.controller;

import Client.connection.AnsiFormatter;
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
    private static final Logger logger = Logger.getLogger(ModeController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    private Button classicButton;

    @FXML
    private Button endlessButton;

    @FXML
    public void initialize() {
        classicButton.setOnAction(actionEvent -> switchToCategorySelection());
        endlessButton.setOnAction(actionEvent -> switchToEndlessMode());
    }

    private void switchToCategorySelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/category_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) classicButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Select a Category");
            stage.setResizable(false);
            stage.show();

            logger.info("Switched to Category Selection.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Category Menu", e);
        }
    }

    private void switchToEndlessMode() {
        logger.info("Endless Mode button clicked. (TODO: Implement Endless Mode)");
        // TODO: Implement Endless Mode if Classic Mode is completed
    }
}
