package App;

import Client.connection.ClientConnection;
import Client.controller.MainMenuController;
import Client.view.MainMenuView;

public class App {
    public static void start() {
        ClientConnection clientConnection = new ClientConnection();
        new MainMenuController(clientConnection);
        new MainMenuView();
    }
}

