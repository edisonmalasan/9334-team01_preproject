package Server.controller;

import Client.model.PlayerModel;
import Server.model.LeaderboardModel;

import java.util.List;

public class LeaderboardController {
    private static LeaderboardModel leaderboard = new LeaderboardModel();

    public static String getLeaderboard() {
        List<PlayerModel> leaderboardEntries = leaderboard.getLeaderboardEntries();
        return String.join( "\n", leaderboardEntries.toString());
    }

    public static void addScore(PlayerModel player) {
        leaderboard.addEntry(player);
    }
}
