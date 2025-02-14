package Client.controller;

import Client.connection.ClientConnection;
import Client.model.PlayerModel;
import common.Response;
import exception.ConnectionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InputUsernameController {
    private PlayerModel player;
    private ClientConnection clientConnection;

    @FXML
    public TextField usernameField;

    @FXML
    public Label errorLabel;

    @FXML
    private Button enterButton;

    public InputUsernameController() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
    }

    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> handleEnterButtonClick());

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });
    }

    private void handleEnterButtonClick() {
        String username = usernameField.getText().trim().toLowerCase();

        if (username.isEmpty()) {
            errorLabel.setText("Username cannot be empty!");
            return;
        }

        player = new PlayerModel(username, 0);
        System.out.println("DEBUG: Sending player data to server: " + player.getName());

        new Thread(() -> {
            try {
                clientConnection.sendObject(player);
                Response response = (Response) clientConnection.receiveObject();

                System.out.println("DEBUG: Received response: " + response);

                Platform.runLater(() -> {
                    if (!response.isSuccess()) {
                        usernameField.clear();
                        usernameField.requestFocus();
                        errorLabel.setText(response.getMessage());
                    } else {
                        switchToGameMode();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> errorLabel.setText("Connection error."));
            }
        }).start();
    }

    private void switchToGameMode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mode_menu.fxml"));
            Scene modeScene = new Scene(loader.load());

            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.setScene(modeScene);
            stage.setTitle("Game Modes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load game mode.");
        }
    }
}
