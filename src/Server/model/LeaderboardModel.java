package Server.model;

import Server.model.XMLStorageModel;
import Client.model.LeaderboardEntryModel;
import java.util.*;

public class LeaderboardModel {
    // use a synchronized list for thread safety
    private List<LeaderboardEntryModel> leaderboard = Collections.synchronizedList(new ArrayList<>());

    public LeaderboardModel() {
        leaderboard = Collections.synchronizedList(XMLStorageModel.loadLeaderboardFromXML("data/leaderboard.xml"));
    }

    public List<String> getLeaderboardEntries() {
        List<String> entries = new ArrayList<>();
        // sync access to the leaderboard list
        synchronized (leaderboard) {
            for (LeaderboardEntryModel entry : leaderboard) {
                entries.add(entry.getPlayerName() + " - " + entry.getScore());
            }
        }
        return entries;
    }

    public void addEntry(String playerName, int score) {
        // synchronize access to the leaderboard list
        synchronized (leaderboard) {
            leaderboard.add(new LeaderboardEntryModel(playerName, score));
            leaderboard.sort((a, b) -> Integer.compare(b.getScore(), a.getScore())); // sort descending (highest to lowest)
            XMLStorageModel.saveLeaderboardToXML("data/leaderboard.xml", leaderboard);
        }
    }
}