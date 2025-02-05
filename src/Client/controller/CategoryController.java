package Client.controller;

import Client.connection.ClientConnection;
import Client.view.GameView;

public class CategoryController {
    private ClientConnection clientConnection;

    public CategoryController(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void handleCategorySelection(String category) {
        String question = clientConnection.sendRequest("GET_QUESTION: " + category);
        if (question != null) {
            new GameView(clientConnection, question);
        } else {
            System.out.println("No question found");
            // (optional) show a toast message here from javafx instead of sout if there is no question in the data
        }
    }
}
