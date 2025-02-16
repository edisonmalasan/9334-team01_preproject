package Client.controller;

import Client.connection.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.model.PlayerModel;
import Client.view.MainMenuView;
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

import java.io.EOFException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputUsernameController {
    private static final Logger logger = Logger.getLogger(InputUsernameController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private PlayerModel player;
    private ClientConnection clientConnection;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

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
            logger.warning("User attempted to enter an empty username.");
            return;
        }

        player = new PlayerModel(username, 0);
        logger.info("Sending player data to server: " + player.getName());

        new Thread(() -> {
            try {
                clientConnection.sendObject(player);
                Response response = (Response) clientConnection.receiveObject();

                logger.info("Received response: " + response);

                Platform.runLater(() -> {
                    if (!response.isSuccess()) {
                        usernameField.clear();
                        usernameField.requestFocus();
                        errorLabel.setText(response.getMessage());
                        logger.warning("Username rejected: " + response.getMessage());
                    } else {
                        switchToMainMenu();
                    }
                });

            } catch (EOFException e) {
                logger.severe("Server closed the connection unexpectedly.");
                Platform.runLater(() -> errorLabel.setText("Server disconnected. Please try again."));

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Connection error occurred", e);
                Platform.runLater(() -> {
                    errorLabel.setText("Connection error.");
                    usernameField.clear();
                });
            }
        }).start();
    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("Successfully switched to the Main Menu.");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Main Menu", e);
            errorLabel.setText("Failed to load Main Menu");
        }
    }
}
