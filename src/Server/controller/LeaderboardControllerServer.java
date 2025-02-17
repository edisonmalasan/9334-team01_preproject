package Server.controller;

import Server.model.LeaderboardEntryModelServer;

import java.util.List;

public class LeaderboardControllerServer {
    private static final String CLASSIC_LEADERBOARD_FILE = "data/classic_leaderboard.xml";
    private static final String ENDLESS_LEADERBOARD_FILE = "data/endless_leaderboard.xml";

    public static List<LeaderboardEntryModelServer> getClassicLeaderboard() {
        return XMLStorageController.loadLeaderboardFromXML(CLASSIC_LEADERBOARD_FILE);
    }

    public static List<LeaderboardEntryModelServer> getEndlessLeaderboard() {
        return XMLStorageController.loadLeaderboardFromXML(ENDLESS_LEADERBOARD_FILE);
    }

    public static void addScore(String playerName, int score, boolean isEndlessMode) {
        String leaderboardFile = isEndlessMode ? ENDLESS_LEADERBOARD_FILE : CLASSIC_LEADERBOARD_FILE;
        List<LeaderboardEntryModelServer> leaderboard = XMLStorageController.loadLeaderboardFromXML(leaderboardFile);

        boolean found = false;
        for (LeaderboardEntryModelServer entry : leaderboard) {
            if (entry.getPlayerName().equals(playerName)) {
                if (score > entry.getScore()) {
                    entry.setScore(score);
                }
                found = true;
                break;
            }
        }

        if (!found) {
            leaderboard.add(new LeaderboardEntryModelServer(playerName, score));
        }

        leaderboard.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        XMLStorageController.saveLeaderboardToXML(leaderboardFile, leaderboard);
    }
}