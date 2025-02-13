package Client.controller;

import Client.connection.ClientConnection;
import common.Response;
import exception.ConnectionException;
import exception.InvalidRequestException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeaderboardController {
    private ClientConnection clientConnection;

    public LeaderboardController() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
    }

    public String getLeaderboard() {
        try {
            Response response = clientConnection.sendRequest("GET_LEADERBOARD");
            if (response.isSuccess()) {
                return response.getData().toString();
            } else {
                return "Error fetching leaderboard: " + response.getMessage();
            }
        } catch (ConnectionException | InvalidRequestException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String addScore(String playerName, int score) {
        try {
            String request = "ADD_SCORE:" + playerName + ":" + score;
            Response response = clientConnection.sendRequest(request);
            if (response.isSuccess()) {
                return "Score added successfully.";
            } else {
                return "Error adding score: " + response.getMessage();
            }
        } catch (ConnectionException | InvalidRequestException e) {
            return "Error: " + e.getMessage();
        }
    }

    public void closeConnection() {
        clientConnection.close();
    }
}
