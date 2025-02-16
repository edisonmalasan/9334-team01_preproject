package Client.utils;

import Client.utils.TimerUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class QTEUtil {
    private final TimerUtil timerUtil;
    private final Pane QTEPane;
    private final Random random = new Random();
    private final Set<Integer> qteTriggers;
    private final Timer timer = new Timer();

    public QTEUtil(int totalQuestions, TimerUtil timerUtil, Pane QTEPane) {
        this.timerUtil = timerUtil;
        this.QTEPane = QTEPane;
        this.qteTriggers = generateQTETriggers(totalQuestions);
    }

    /**
     * Generates random points in the quiz where Quick Time Events (QTEs) will trigger.
     */
    private Set<Integer> generateQTETriggers(int totalQuestions) {
        Set<Integer> qteTriggers = new HashSet<>();
        int qteCount = Math.min(3, totalQuestions); // Max 3 QTEs per game

        while (qteTriggers.size() < qteCount) {
            qteTriggers.add(random.nextInt(totalQuestions));
        }
        return qteTriggers;
    }

    /**
     * Triggers a Quick Time Event (QTE) by displaying a "Defuse" button at a random position.
     */
    public void triggerQuickTimeEvent(int currentQuestionIndex) {
        if (!qteTriggers.contains(currentQuestionIndex)) return; // Only trigger at predefined points

        Platform.runLater(() -> {
            Button defuseButton = new Button();
            defuseButton.setStyle("-fx-background-image: url('/images/defuse_button.png'); " +
                    "-fx-background-size: contain; " +
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-color: transparent; " +
                    "-fx-pref-width: 200px; -fx-pref-height: 80px;");

            // 🎯 Get QTEPane dimensions to calculate random position
            double maxX = QTEPane.getPrefWidth() - 200;  // Ensure button stays within bounds
            double maxY = QTEPane.getPrefHeight() - 80;

            // 🔥 Random position
            double randomX = random.nextDouble() * maxX;
            double randomY = random.nextDouble() * maxY;

            defuseButton.setLayoutX(randomX);
            defuseButton.setLayoutY(randomY);
            QTEPane.getChildren().add(defuseButton);

            // Timer for QTE failure
            TimerTask failTask = new TimerTask() {
                @Override
                public void run() {
                    if (QTEPane.getChildren().contains(defuseButton)) {
                        Platform.runLater(() -> {
                            QTEPane.getChildren().remove(defuseButton);
                            int penalty = determineQTEPenalty();
                            timerUtil.subtractTime(penalty);
                        });
                    }
                }
            };
            timer.schedule(failTask, 3000); // 3 seconds to react

            // If clicked in time, change image & remove
            defuseButton.setOnAction(e -> {
                defuseButton.setStyle("-fx-background-image: url('/images/defuse_button_clicked.png'); "  +
                        "-fx-background-size: contain; " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-color: transparent; " +
                        "-fx-pref-width: 200px; -fx-pref-height: 80px;");

                // Delay before removal for smooth effect
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> QTEPane.getChildren().remove(defuseButton));
                    }
                }, 300);
                failTask.cancel();
            });
        });
    }

    /**
     * Determines how much time to deduct when a QTE is failed.
     */
    private int determineQTEPenalty() {
        if (timerUtil.getSeconds() > 90) return 30;  // 30-second penalty if time > 90s
        if (timerUtil.getSeconds() > 45) return 20;  // 20-second penalty if time > 45s
        return 15;                                   // 15-second penalty otherwise
    }
}
