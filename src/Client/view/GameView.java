package Client.view;

import Client.connection.ClientConnection;
import Server.model.QuestionBankModel;
import common.model.QuestionModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private ClientConnection clientConnection;
    private QuestionModel question;
    public JTextArea questionText;
    public JButton option1, option2, option3, option4;


    public GameView(ClientConnection clientConnection, QuestionModel question) {
        this.clientConnection = clientConnection;
        this.question = question;

    }

    public GameView(ClientConnection clientConnection) {
        setTitle("Game View");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(new GridLayout(2,5));

        questionText = new JTextArea();
        option1 = new JButton("1");
        option2 = new JButton("2");
        option3 = new JButton("3");
        option4 = new JButton("4");

        add(questionText);
        add(option1);
        add(option2);
        add(option3);
        add(option4);

    }

    public JTextArea getQuestionText() {
        return questionText;
    }


}
