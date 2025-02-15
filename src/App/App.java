package App;

import Client.connection.ClientConnection;
import Client.controller.InputUsernameController;
import exception.ConnectionException;
import exception.FXMLLoadingException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/input_username.fxml"));
            Parent root = fxmlLoader.load();

            try {
                ClientConnection.getInstance().connect();
                System.out.println("Client connected to the server.");
            } catch (ConnectionException e) {
                System.err.println("⚠ Server is not running. The client will continue in offline mode.");
            }

            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/bomb_mad.png")));

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Bomb Defusing Game");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            throw new FXMLLoadingException("input_username.fxml", e);
        }
    }
}
