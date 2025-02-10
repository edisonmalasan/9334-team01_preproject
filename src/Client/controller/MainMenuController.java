package Client.controller;

import Client.connection.ClientConnection;
import Client.view.InputUsernameView;
import Client.view.LeaderboardView;
import Client.view.MainMenuView;
import Client.view.GameView;

import java.awt.event.ActionEvent;

public class MainMenuController {
    private MainMenuView view;
    private ClientConnection clientConnection;

    public MainMenuController(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.view = new MainMenuView();

        view.getPlayButton().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.playButton){
                view.dispose();
                InputUsernameController inputUsernameController = new InputUsernameController(clientConnection);
            }
        });

        view.getLeaderboardButton().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.leaderboardButton){
                view.dispose();
                LeaderboardView leaderboardView = new LeaderboardView();

            }
        });

        view.getExitButton().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.exitButton){
                System.exit(0);
            }
        });

        // TODO: Setup Buttons here in view like
        //  view.getPlayButton().addActionListener(e ->  openGameModeView()); etc..
    }
}
