package Client.model;

import java.time.format.DateTimeFormatter;

public class ComboModel {
    private DateTimeFormatter time;
    private int numberOfCombo;
    private int score;

    public ComboModel(DateTimeFormatter time, int numberOfCombo, int score) {
        this.time = time;
        this.numberOfCombo = numberOfCombo;
        this.score = score;
    }

    public DateTimeFormatter getTime() {
        return time;
    }

    public void setTime(DateTimeFormatter time) {
        this.time = time;
    }

    public int getNumberOfCombo() {
        return numberOfCombo;
    }

    public void setNumberOfCombo(int numberOfCombo) {
        this.numberOfCombo = numberOfCombo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ComboModel{" +
                "time=" + time +
                ", numberOfCombo=" + numberOfCombo +
                ", score=" + score +
                '}';
    }
}
