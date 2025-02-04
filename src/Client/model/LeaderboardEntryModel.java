package Client.model;

import java.io.Serializable;

public class LeaderboardEntryModel implements Serializable {
    private String playerName;
    private int score;

    public LeaderboardEntryModel(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
