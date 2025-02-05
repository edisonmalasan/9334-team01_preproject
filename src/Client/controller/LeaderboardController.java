package Client.controller;

import Client.connection.ClientConnection;

public class LeaderboardController {
    private ClientConnection clientConnection;

    public LeaderboardController() {
        this.clientConnection = new ClientConnection();
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
