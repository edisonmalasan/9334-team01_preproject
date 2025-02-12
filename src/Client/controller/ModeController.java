package Client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ModeController {
    @FXML
    public ImageView gamemodeLabel;
    @FXML
    public Button classicButton;
    @FXML
    public Button endlessButton;
    @FXML
    public AnchorPane categoryMenu;

    @FXML
    public void initialize() {
        classicButton.setOnAction(event -> switchToCategoryScreen());
    }

    private void switchToCategoryScreen() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/category_menu.fxml"));
//            Scene categoryScene = new Scene(loader.load());
//            Stage stage = (Stage) classicButton.getScene().getWindow();
//            stage.setScene(categoryScene);
//            stage.setTitle("Categories");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
