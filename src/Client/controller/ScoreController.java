package Client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreController {
    private static final Logger logger = Logger.getLogger(ScoreController.class.getName());

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label scoreLabel;

    @FXML
    public void initialize() {
        backToMenuButton.setOnAction(actionEvent -> switchToMainMenu());
    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mode_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.setResizable(false);
            stage.show();

            logger.info("Switched to Main Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Main Menu", e);
        }
    }

}
