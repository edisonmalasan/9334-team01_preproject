package Server.controller;

import Server.model.LeaderboardEntryModelServer;

import java.util.List;

public class LeaderboardControllerServer {
    private static final String LEADERBOARD_FILE = "data/leaderboard.xml";

    public static List<LeaderboardEntryModelServer> getLeaderboard() {
        return XMLStorageController.loadLeaderboardFromXML(LEADERBOARD_FILE);
    }

    public static void addScore(String playerName, int score) {
        List<LeaderboardEntryModelServer> leaderboard = XMLStorageController.loadLeaderboardFromXML("data/leaderboard.xml");

        boolean found = false;
        for (LeaderboardEntryModelServer entry : leaderboard) {
            if (entry.getPlayerName().equals(playerName)) {
                entry.setScore(entry.getScore() + score);
                found = true;
                break;
            }
        }

        if (!found) {
            leaderboard.add(new LeaderboardEntryModelServer(playerName, score));
        }

        leaderboard.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        XMLStorageController.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);
    }

}
