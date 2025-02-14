package Client.controller;

import Client.connection.ClientConnection;
import Client.model.PlayerModel;
import common.Response;
import exception.ConnectionException;
import exception.InvalidRequestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InputUsernameController {
    public PlayerModel player;
    private ClientConnection clientConnection;

    @FXML
    public TextField usernameField;

    @FXML
    public Label errorLabel;

    @FXML
    private Button enterButton;

    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> {
            try {
                handleEnterButtonClick();
            } catch (IOException e) {
                errorLabel.setText("Error: " + e.getMessage());
            }
        });
    }

    public InputUsernameController() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
    }

    private void handleEnterButtonClick() throws IOException {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            errorLabel.setText("Username cannot be empty!");
            return;
        }

        player = new PlayerModel(username, 0);

        try {
            // Send the PlayerModel to the server
            clientConnection.sendObject(player);

            Response response = (Response) clientConnection.receiveObject();

            if (!response.isSuccess()) {
                errorLabel.setText("Error: " + response.getMessage());
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Connection error.");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/gamemode_menu.fxml"));
        Scene modeScene = new Scene(loader.load());

        Stage stage = (Stage) enterButton.getScene().getWindow();
        stage.setScene(modeScene);
        stage.setTitle("Game Modes");
        stage.show();
    }

}