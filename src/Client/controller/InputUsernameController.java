package Client.controller;

import Client.connection.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.view.ViewManager;
import exception.ConnectionException;
import exception.InvalidUsernameException;

import exception.ServerNotRunningException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputUsernameController {
    private static final Logger logger = Logger.getLogger(InputUsernameController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private static String playerName;

    private ClientConnection clientConnection;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button enterButton;

    public InputUsernameController() {
        try {
            this.clientConnection = ClientConnection.getInstance();

            if (this.clientConnection == null) {
                logger.severe("❌ ERROR: ClientConnection is NULL! Server may not be running.");
            } else {
                logger.info("✅ Client is set up properly.");
            }

        } catch (ConnectionException e) {
            handleException(new ServerNotRunningException("⚠ Server is not running."));
        }
    }


    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> handleEnterButtonClick(event));

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });
    }

    private void handleEnterButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim().toLowerCase();

        if (username.isEmpty()) {
            handleException(new InvalidUsernameException("Username cannot be empty!"));
            return;
        }

        playerName = username;
        switchToMainMenu(event);
    }

    public static String getPlayerName() {
        return playerName;
    }

    private void switchToMainMenu(ActionEvent event) {
        ViewManager.goTo(event, ViewManager.MAIN_MENU, "Main Menu");
    }

    private void handleException(Exception e) {
        logger.severe("❌ ERROR: " + e.getMessage());
        Platform.runLater(() -> {
            usernameField.clear();
            usernameField.requestFocus();
            errorLabel.setText(e.getMessage());
        });
    }

}
