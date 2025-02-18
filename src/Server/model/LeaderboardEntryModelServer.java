package Server.model;

import java.io.Serializable;

/**
 * Represents an entry to the leaderboard (server side)
 */
public class LeaderboardEntryModelServer implements Serializable {
    private String playerName;
    private int score;

    public LeaderboardEntryModelServer(String playerName, int score) {
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
        return playerName + ": " + score;
    }
}