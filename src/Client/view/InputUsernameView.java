package Client.view;

//TODO: For testing only, to be improved

import javax.swing.*;
import java.awt.*;

public class InputUsernameView extends JFrame {

    public JLabel enterUsernameLabel;
    public JTextField usernameField;
    public JButton enterButton;
    public InputUsernameView() {
        setTitle("Username Input");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(new GridLayout(3,1));

        enterUsernameLabel = new JLabel("Enter your username:");
        usernameField = new JTextField();
        enterButton = new JButton("Enter");

        add(enterUsernameLabel);
        add(usernameField);
        add(enterButton);
    }

    public JTextField getUsernameField (){
        return usernameField;
    }
    public JButton getEnterButton() {
        return enterButton;
    }
}