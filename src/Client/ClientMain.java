package Client;

import Client.connection.ClientConnection;
import Client.view.MainMenuView;

public class ClientMain {
    public static void main(String[] args) {
        // Create a connection to the server
        ClientConnection clientConnection = new ClientConnection();

        // Open the main menu (GUI)
        new MainMenuView(clientConnection);
    }
}
