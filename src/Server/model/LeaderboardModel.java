package Server.model;

import Server.model.XMLStorageModel;
import java.util.*;

public class LeaderboardModel {
    private List<LeaderboardEntry> leaderboard;

    public LeaderboardModel() {
        leaderboard = XMLStorageModel.loadLeaderboardFromXML("data/leaderboard.xml");
    }

    public List<String> getLeaderboardEntries() {
        List<String> entries = new ArrayList<>();
        for (LeaderboardEntry entry : leaderboard) {
            entries.add(entry.getPlayerName() + " - " + entry.getScore());
        }
        return entries;
    }

    public void addEntry(String playerName, int score) {
        leaderboard.add(new LeaderboardEntry(playerName, score));
        leaderboard.sort((a, b) -> Integer.compare(b.getScore(), a.getScore())); // Sort descending
        XMLStorageModel.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);
    }
}
