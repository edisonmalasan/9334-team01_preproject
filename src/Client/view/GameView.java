package Client.view;

import Client.connection.ClientConnection;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

public class GameView {
    private ClientConnection clientConnection;
    private QuestionModel question;

    public GameView(ClientConnection clientConnection, QuestionModel question) {
        this.clientConnection = clientConnection;
        this.question = question;
    }

    public GameView() {

    }

}
