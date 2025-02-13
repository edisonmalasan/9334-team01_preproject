package App;    

import Client.connection.ClientConnection;
import Client.controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch();
    }

    // loading fxml files should be done in view classes
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
//        Parent root = loader.load();
//        stage.setScene(new Scene(root));
//        stage.setTitle("Main Menu");
//        stage.show();
//    }
}