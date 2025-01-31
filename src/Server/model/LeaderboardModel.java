package Server.model;

import java.util.*;
import Client.model.LeaderboardEntryModel;

public class LeaderboardModel {
    private List<LeaderboardEntryModel> leaderboard;

    public LeaderboardModel() {
        leaderboard = new ArrayList<>();
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        // Example data (this could be loaded from XML)
        leaderboard.add(new LeaderboardEntryModel("Player1", 80));
        leaderboard.add(new LeaderboardEntryModel("Player2", 70));
        leaderboard.add(new LeaderboardEntryModel("Player3", 50));
    }

    public List<LeaderboardEntryModel> getLeaderboard() {
        return leaderboard;
    }

    public void addEntry(LeaderboardEntryModel entry) {
        leaderboard.add(entry);
        leaderboard.sort(Comparator.comparingInt(LeaderboardEntryModel::getScore).reversed());
    }
}
