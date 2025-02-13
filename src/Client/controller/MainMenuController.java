package Client.controller;

import Client.view.MainMenuView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        playButton.setOnAction( actionEvent -> mainMenuView.switchScene("/views/input_username.fxml", "Bomb Defusing Game"));
        leaderboardButton.setOnAction(actionEvent -> mainMenuView.switchScene("/views/leaderboard.fxml", "Leaderboard"));
        quitButton.setOnAction(actionEvent -> System.exit(0));
    }
}
