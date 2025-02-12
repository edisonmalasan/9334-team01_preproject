package Client.controller;

import Client.model.PlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

public class InputUsernameController {

    @FXML
    public TextField usernameField;
    @FXML
    public Label errorLabel;
    @FXML
    private Button enterButton;

    private PlayerModel player;

    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> {
            try {
                handleEnterButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleEnterButtonClick() throws IOException {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            usernameField.setPromptText("Username cannot be empty!"); // Show error message
            return; // Stop further execution
        }

        player = new PlayerModel(username, 0);

        try {
            File inputFile = new File("data/leaderboard.xml");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("entry");

            Element root = document.getDocumentElement();
            Collection<PlayerModel> players = new ArrayList<>();
            players.add(player);

            boolean check = false;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Element element = (Element) node;
                String name = element.getElementsByTagName("player").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());

                if (name.equals(player.getName())) {
                    player.setScore(score);
                    check = true;
                    break;
                }
            }

            if (!check) {
                for (PlayerModel playerModel : players) {
                    Element newPlayer = document.createElement("entry");

                    Element name = document.createElement("player");
                    name.appendChild(document.createTextNode(playerModel.getName()));
                    newPlayer.appendChild(name);

                    Element score = document.createElement("score");
                    score.appendChild(document.createTextNode(Integer.toString(playerModel.getScore())));
                    newPlayer.appendChild(score);

                    root.appendChild(newPlayer);
                }
                writeDOMToFile(document, "data/leaderboard.xml");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gamemode_menu.fxml"));
        Scene modeScene = new Scene(loader.load());

        Stage stage = (Stage) enterButton.getScene().getWindow();
        stage.setScene(modeScene);
        stage.setTitle("Game modes");
        stage.show();
    }

    private static void writeDOMToFile(Document document, String fileName) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            PrintWriter fileWriter = new PrintWriter(fileName);
            StreamResult result = new StreamResult(fileWriter);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
