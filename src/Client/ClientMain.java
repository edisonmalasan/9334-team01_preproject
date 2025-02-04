package Client;

import Client.connection.ClientConnection;
import Client.view.MainMenuView;

public class ClientMain {
    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection();

        new MainMenuView(clientConnection);
    }
}
