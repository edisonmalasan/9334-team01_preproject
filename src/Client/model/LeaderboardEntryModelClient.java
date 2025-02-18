package Client.model;

import java.io.Serializable;

/**
 * Represents a leaderboard entry (client side)
 */
public class LeaderboardEntryModelClient implements Serializable {
    private String playerName;
    private int score;

    public LeaderboardEntryModelClient(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public LeaderboardEntryModelClient(PlayerModel player) {
        this.playerName = player.getName();
        this.score = player.getScore();
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
