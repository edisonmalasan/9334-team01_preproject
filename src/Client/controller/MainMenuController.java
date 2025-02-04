package Client.controller;

import Client.connection.ClientConnection;
import Client.view.LeaderboardView;
import Client.view.MainMenuView;
import Client.view.GameModeView;

public class MainMenuController {
    private MainMenuView view;
    private ClientConnection clientConnection;

    public MainMenuController(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.view = new MainMenuView();

        view.getPlayButton().addActionListener(e -> openGameModeView());
        view.getLeaderboardButton().addActionListener(e -> showLeaderboard());
        view.getExitButton().addActionListener(e -> System.exit(0));
    }

    private void openGameModeView() {
        new GameModeView(clientConnection);
        view.dispose();
    }

    private void showLeaderboard() {
        new LeaderboardView(clientConnection);
        view.dispose();
    }
}
