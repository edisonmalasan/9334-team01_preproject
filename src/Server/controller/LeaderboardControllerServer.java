package Server.controller;

import Server.model.LeaderboardEntryModelServer;
import Server.model.XMLStorageModel;

import java.util.List;

public class LeaderboardControllerServer {
    private static final String LEADERBOARD_FILE = "data/leaderboard.xml";

    public static String getLeaderboard() {
        List<LeaderboardEntryModelServer> leaderboard = XMLStorageModel.loadLeaderboardFromXML(LEADERBOARD_FILE);
        StringBuilder leaderboardString = new StringBuilder();

        for (LeaderboardEntryModelServer entry : leaderboard) {
            leaderboardString.append(entry.getPlayerName()).append(": ").append(entry.getScore()).append("\n");
        }

        return leaderboardString.toString();
    }

    public static void addScore(String playerName, int score) {
        List<LeaderboardEntryModelServer> leaderboard = XMLStorageModel.loadLeaderboardFromXML("data/leaderboard.xml");

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
        XMLStorageModel.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);
    }

}
