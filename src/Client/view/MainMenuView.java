package Client.view;

import javax.swing.*;
import java.awt.*;

// SAMPLE ONLI
public class MainMenuView extends JFrame {
    public JButton playButton;
    public JButton leaderboardButton;
    public JButton exitButton;

    public MainMenuView() {
        setTitle("Bomb Defuse Game - Main Menu");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(3,1)); // grid layout is like VStack a React component

        playButton = new JButton("PLAY");
        leaderboardButton = new JButton("LEADERBOARD");
        exitButton = new JButton("EXIT");

        add(playButton);
        add(leaderboardButton);
        add(exitButton);
    }

    public JButton getPlayButton() {
        return playButton;
    }
    public JButton getLeaderboardButton() {
        return leaderboardButton;
    }
    public JButton getExitButton() {
        return exitButton;
    }
}
