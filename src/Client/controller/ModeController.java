package Client.controller;

import Client.view.CategoryView;
import Client.view.ModeView;
import javafx.application.Platform;
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

    private ModeView modeView;

    public void setModeView(ModeView modeView) {
        this.modeView = modeView;
    }

    @FXML
    public void initialize() {
        classicButton.setOnAction(actionEvent -> switchToCategorySelection());
        endlessButton.setOnAction(actionEvent -> switchToEndlessMode());
    }

    private void switchToCategorySelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/category_menu.fxml"));
            Parent root = loader.load();

            CategoryController controller = loader.getController();
            CategoryView categoryView = new CategoryView((Stage) classicButton.getScene().getWindow());
            controller.setCategoryView(categoryView);

            Stage stage = (Stage) classicButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToEndlessMode() {
        // TODO: IF CLASSIC MODE IS DONE BY SUNDAY
    }

}
