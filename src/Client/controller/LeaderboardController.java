package Client.controller;

import Client.connection.ClientConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeaderboardController {
    @FXML
    public Button backToMenuButton;
    @FXML
    public Label scoreLabel;
    private ClientConnection clientConnection;

    public LeaderboardController() {
        this.clientConnection = ClientConnection.getInstance();
    }

    public String getLeaderboard() {
        String response = clientConnection.sendRequest("GET_LEADERBOARD");

        if (response.isEmpty()) {
            return response;
        } else {
            return "Error fetching leaderboard";
        }
    }

    public String addScore(String playerName, int score) {
        String request = "ADD_SCORE" + playerName + ": " + score;
        String response = clientConnection.sendRequest(request);

        if (response.isEmpty()) {
            return response;
        } else {
            return "Error adding score";
        }
    }

    public void closeConnection() {
        clientConnection.close();
    }
}
