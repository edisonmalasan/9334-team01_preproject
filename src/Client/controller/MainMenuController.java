package Client.controller;

import Client.view.CategoryView;
import Client.view.MainMenuView;
import Client.view.ModeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button quitButton;

    private MainMenuView mainMenuView;

    public void setMainMenuView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    public void initialize() {
        playButton.setOnAction(actionEvent -> switchToModeMenu());

        leaderboardButton.setOnAction(actionEvent -> switchToLeaderboard());

        quitButton.setOnAction(actionEvent -> System.exit(0));
    }

    private void switchToModeMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mode_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToLeaderboard() {
        // TODO: Sunday
    }

}
