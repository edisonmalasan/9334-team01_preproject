package Client.model;

import java.util.Objects;

public class PlayerModel {
    private String name;
    private int score;

    public PlayerModel(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerModel that = (PlayerModel) obj;
        return score == that.score && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public String toString() {
        return name + " - Score: " + score;
    }
}
