package App;    

import Client.controller.MainMenuController;
import Client.view.MainMenuView;
import exception.FXMLLoadingException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resources/views/main_menu.fxml"));
            Parent root = fxmlLoader.load();

            MainMenuController controller = fxmlLoader.getController();
            MainMenuView mainMenuView = new MainMenuView(primaryStage);
            controller.setMainMenuView(mainMenuView);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Bomb Defusing Game");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new FXMLLoadingException("main_menu.fxml", e);
        }
    }
}