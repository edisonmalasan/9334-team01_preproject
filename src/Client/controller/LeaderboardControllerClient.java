package Client.controller;

import Client.connection.ClientConnection;
import Client.model.LeaderboardEntryModelClient;
import Client.model.PlayerModel;
import common.Response;
import exception.ConnectionException;
import exception.InvalidRequestException;

import java.io.IOException;
import java.util.List;

public class LeaderboardControllerClient {
    private ClientConnection clientConnection;

    public LeaderboardControllerClient() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
    }

    public String getLeaderboard() {
        try {
            // send obj req
            clientConnection.sendObject("GET_LEADERBOARD");

            // receive the response obj from server
            Response response = (Response) clientConnection.receiveObject();

            if (response.isSuccess()) {
                return response.getData().toString();
            } else {
                return "Error fetching leaderboard: " + response.getMessage();
            }
        } catch (IOException | ClassNotFoundException e) {
            return "Unable to connect to the server. Please try again later.";
        }
    }

    public String addScore(String playerName, int score) {
        try {
            // create a new instance of leaderboardentrymodelclient
            LeaderboardEntryModelClient entry = new LeaderboardEntryModelClient(playerName, score);

            // send the entry object to the server
            clientConnection.sendObject(entry);

            // receive response from the server
            Response response = (Response) clientConnection.receiveObject();

            if (response.isSuccess()) {
                return "Score added successfully.";
            } else {
                return "Error adding score: " + response.getMessage();
            }
        } catch (IOException | ClassNotFoundException e) {
            return "Error: " + e.getMessage();
        }
    }

    public void closeConnection() {
        clientConnection.close();
    }
}
