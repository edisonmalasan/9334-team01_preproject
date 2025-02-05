package App;

import Client.connection.ClientConnection;
import Client.controller.MainMenuController;

public class App {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        ClientConnection clientConnection = new ClientConnection();
        new MainMenuController(clientConnection);
    }
}