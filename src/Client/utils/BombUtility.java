package Client.utils;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.List;

public class BombUtility {
    private final ImageView bombImage;
    private final ImageView flame;
    private final Line wick;
    private final Label timerLabel;
    private final Runnable explosionCallback;
    private final List<Button> choiceButtons;

    private Timeline wickAnimation;
    private TranslateTransition flameFlicker;
    private Timeline bombTimer;
    private int totalTime = 30;
    private int remainingTime;
    private boolean hasExploded = false;
    private boolean isRunning = false;


    public BombUtility(ImageView bombImage, ImageView flame, Line wick, Label timerLabel,
                       Runnable explosionCallback, List<Button> choiceButtons) {
        this.bombImage = bombImage;
        this.flame = flame;
        this.wick = wick;
        this.timerLabel = timerLabel;
        this.explosionCallback = explosionCallback;
        this.choiceButtons = choiceButtons;
    }

    public void startBombAnimation() {
        if (isRunning) return;
        isRunning = true;

        remainingTime = totalTime;
        updateTimerLabel();
        bombImage.setVisible(true);
        flame.setVisible(true);
        wick.setVisible(true);

        flameFlicker = new TranslateTransition(Duration.millis(200), flame);
        flameFlicker.setFromX(-2);
        flameFlicker.setToX(2);
        flameFlicker.setAutoReverse(true);
        flameFlicker.setCycleCount(Animation.INDEFINITE);
        flameFlicker.play();

        wickAnimation = new Timeline(new KeyFrame(Duration.seconds(1), e -> shortenWick()));
        wickAnimation.setCycleCount(totalTime);
        wickAnimation.play();

        bombTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        bombTimer.setCycleCount(totalTime);
        bombTimer.setOnFinished(e -> triggerExplosion());
        bombTimer.play();
    }

    public void applyPenalty(int penalty) {
        double wickLength = wick.getEndX() - wick.getStartX();
        double shrinkAmount = wickLength / (totalTime / penalty);

        if (wickLength > 0) {
            wick.setStartX(wick.getStartX() + shrinkAmount);
            flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
        }

        remainingTime = Math.max(0, remainingTime - penalty);
        updateTimerLabel();

        if (remainingTime <= 0 || wick.getStartX() >= wick.getEndX()) {
            triggerExplosion();
        }
    }

    private void shortenWick() {
        double wickLength = wick.getEndX() - wick.getStartX();
        double shrinkAmount = wickLength / totalTime;

        if (wick.getStartX() < wick.getEndX()) {
            wick.setStartX(wick.getStartX() + shrinkAmount);
            flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
        }
    }

    private void updateTimer() {
        if (remainingTime > 0) {
            remainingTime--;
            updateTimerLabel();
        }

        if (remainingTime <= 0) {
            triggerExplosion();
        }
    }

    private void updateTimerLabel() {
        Platform.runLater(() -> timerLabel.setText(remainingTime + "s"));
    }

    public void stopBombAnimation() {
        if (wickAnimation != null) {
            wickAnimation.stop();
            wickAnimation = null;
        }
        if (flameFlicker != null) {
            flameFlicker.stop();
            flameFlicker = null;
        }
        if (bombTimer != null) {
            bombTimer.stop();
            bombTimer = null;
        }

        flame.setVisible(false);
        wick.setVisible(false);
    }

    private void triggerExplosion() {
        if (hasExploded) return;
        hasExploded = true;
        stopBombAnimation();
        bombImage.setImage(new Image("/images/explosion.png"));
        System.out.println("BOOM! The bomb explodes!");

        for (Button btn : choiceButtons) {
            btn.setDisable(true);
            btn.setOpacity(0.8);
        }

        Platform.runLater(explosionCallback);
    }

    public boolean isRunning() {
        return isRunning;
    }


}
