package Client.controller;

import Client.connection.ClientConnection;
import Client.model.PlayerModel;
import Client.view.InputUsernameView;
import Client.view.MainMenuView;
import Client.view.ModeView;
import common.Response;
import exception.ConnectionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private InputUsernameView inputUsernameView;

    public InputUsernameController() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
    }

    public void setInputUsernameView(InputUsernameView inputUsernameView) {
        this.inputUsernameView = inputUsernameView;
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
                        updateUI(this::switchToMainMenu);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    errorLabel.setText("Connection error.");
                    usernameField.clear();
                });

                usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
                    errorLabel.setText("");
                });
            }
        }).start();
    }

    private void updateUI(Runnable action) {
        javafx.application.Platform.runLater(action);
    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            MainMenuController mainMenuController = loader.getController();
            MainMenuView mainMenuView = new MainMenuView((Stage) enterButton.getScene().getWindow());
            mainMenuController.setMainMenuView(mainMenuView);

            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load Main Menu");
        }
    }

}
