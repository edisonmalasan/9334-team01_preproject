package Server.controller;

import Server.model.LeaderboardModel;

import java.util.List;

public class LeaderboardController {
    private static LeaderboardModel leaderboard = new LeaderboardModel();

    public static String getLeaderboard() {
        List<String> leaderboardEntries = leaderboard.getLeaderboardEntries();
        return String.join("\n", leaderboardEntries);
    }

    public static void addScore(String playerName, int score) {
        leaderboard.addEntry(playerName, score);
    }
}
