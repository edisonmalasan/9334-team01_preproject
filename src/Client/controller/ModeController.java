package Client.controller;

import Client.view.CategoryView;
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
        classicButton.setOnAction(actionEvent -> {
            if (modeView != null) {
                CategoryView categoryView = new CategoryView(modeView.getStage());
                categoryView.switchScene("/views/category_menu.fxml", "Select a Category");
            } else {
                System.out.println("ERROR: ModeView is not set!");
            }
        });

        endlessButton.setOnAction(actionEvent -> {
            if (modeView != null) {
                modeView.switchScene("/views/endless_game.fxml", "Endless Mode");
            } else {
                System.out.println("ERROR: ModeView is not set!");
            }
        });
    }
}
