package Client.utility;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtility {
    private int secondsRemaining;
    private Timer timer;
    private boolean isRunning = false;
    private Label timerLabel;

    public TimerUtility(int durationInSeconds, Label timerLabel) {
        this.secondsRemaining = durationInSeconds;
        this.timerLabel = timerLabel;
    }

    public int getSecondsRemaining() {
        this.secondsRemaining--;
        return this.secondsRemaining;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateTimerUI());
                if (secondsRemaining <= 0) {
                    stop();
                    Platform.runLater(() -> timerLabel.setText("Time's up!"));
                }
                secondsRemaining--;
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        isRunning = false;
    }

    public void addTime(int seconds) {
        secondsRemaining += seconds;
        updateTimerUI();
    }

    public void subtractTime(int seconds) {
        secondsRemaining = Math.max(0, secondsRemaining - seconds);
        updateTimerUI();
    }

    public void reset(int newDuration) {
        stop();
        secondsRemaining = newDuration;
        updateTimerUI();
    }

    private void updateTimerUI() {
        String timeFormatted = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
        timerLabel.setText(timeFormatted);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getSeconds() {
        return secondsRemaining;
    }
}
