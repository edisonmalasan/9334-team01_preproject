package Client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button playButton;
    @FXML
    private Button leaderboardButton;
    @FXML
    private Button quitButton;

    @FXML
    public void initialize() {
        playButton.setOnAction(event -> switchToLoginScreen());
        quitButton.setOnAction(event -> System.exit(0)); // Closes the application
    }

    private void switchToLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user_login.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
