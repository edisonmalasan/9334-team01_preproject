package Client.controller;

import Client.connection.ClientConnection;
import Client.view.CategoryView;
import Client.view.GameView;
import Client.view.InputUsernameView;
import Client.model.PlayerModel;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;


public class InputUsernameController {
    private InputUsernameView view;
    public PlayerModel player;

    public InputUsernameController(ClientConnection clientConnection) {
        this.view = new InputUsernameView();

        view.getEnterButton().addActionListener((ActionEvent e) -> {
            if (e.getSource() == view.enterButton){
                player = new PlayerModel(view.getUsernameField().getText(),0);

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

                    if (!check){
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
                        writeDOMToFile(document,"data/leaderboard.xml");
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                view.dispose();
                CategoryController categoryController = new CategoryController(clientConnection);
            }
        });
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