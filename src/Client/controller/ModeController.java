package Client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ModeController {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToEndlessMode() {
        // TODO: Implement Endless Mode if Classic Mode is completed
    }
}
