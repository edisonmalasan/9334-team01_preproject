package Client.model;

import java.io.Serializable;

public class LeaderboardEntryModel {
    private PlayerModel newPlayer;

    public LeaderboardEntryModel(PlayerModel newPlayer) {
        this.newPlayer = newPlayer;
    }

    public String getPlayerName() {
        return newPlayer.getName();
    }

    public int getScore() {
        return newPlayer.getScore();
    }

    public void setPlayerName(String playerName) {
        this.newPlayer.setName(playerName);
    }

    public void setScore(int score) {
        this.newPlayer.setScore(score);
    }

    @Override
    public String toString() {
        return "LeaderboardEntryModel{" +
                "playerName='" + newPlayer.getName() + '\'' +
                ", score=" + newPlayer.getScore() +
                '}';
    }
}
