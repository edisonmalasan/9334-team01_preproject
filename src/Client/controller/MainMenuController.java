package Client.controller;

import Client.view.MainMenuView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
        playButton.setOnAction(actionEvent -> {
            if (mainMenuView != null) {
                mainMenuView.switchScene("/views/mode_menu.fxml", "Bomb Defusing Game");
            } else {
                System.err.println("ERROR: MainMenuView is not set!");
            }
        });

        leaderboardButton.setOnAction(actionEvent -> {
            if (mainMenuView != null) {
                mainMenuView.switchScene("/views/leaderboard.fxml", "Leaderboard");
            } else {
                System.err.println("ERROR: MainMenuView is not set!");
            }
        });

        quitButton.setOnAction(actionEvent -> System.exit(0));
    }

}
