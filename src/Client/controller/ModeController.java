package Client.controller;

import Client.view.ModeView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ModeController {
    @FXML
    private Button classicButton;

    @FXML
    private Button endlessButton;

    private ModeView modeView;

    public void setModeView(ModeView modeView) {
        this.modeView = modeView;
    }

    @FXML
    public void initialize() {
        classicButton.setOnAction(actionEvent -> modeView.switchScene("/views/classic_game.fxml", "Classic Mode"));
        endlessButton.setOnAction(actionEvent -> modeView.switchScene("/views/endless_game.fxml", "Endless Mode"));
    }
}
