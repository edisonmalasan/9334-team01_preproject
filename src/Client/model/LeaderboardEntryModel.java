package Client.model;

import java.io.Serializable;

public class LeaderboardEntryModel {
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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "LeaderboardEntryModel{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                '}';
    }
}
