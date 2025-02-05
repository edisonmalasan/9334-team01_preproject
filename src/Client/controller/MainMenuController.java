package Client.controller;

import Client.connection.ClientConnection;
import Client.view.LeaderboardView;
import Client.view.MainMenuView;
import Client.view.GameView;

public class MainMenuController {
    private MainMenuView view;
    private ClientConnection clientConnection;

    public MainMenuController(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.view = new MainMenuView();


        // TODO: Setup Buttons here in view like
        //  view.getPlayButton().addActionListener(e ->  openGameModeView()); etc..
    }
}
