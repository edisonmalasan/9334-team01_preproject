package Server.model;

import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;

import Client.model.LeaderboardEntryModel;

public class XMLStorageModel {
    public static List<LeaderboardEntryModel> loadLeaderboardFromXML(String filename) {
        List<LeaderboardEntryModel> leaderboard = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) return leaderboard; // Return empty if no file

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String playerName = element.getElementsByTagName("player").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());
                leaderboard.add(new LeaderboardEntryModel(playerName, score));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    public static void saveLeaderboardToXML(String filename, List<LeaderboardEntryModel> leaderboard) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("leaderboard");
            doc.appendChild(rootElement);

            for (LeaderboardEntryModel entry : leaderboard) {
                Element entryElement = doc.createElement("entry");

                Element player = doc.createElement("player");
                player.appendChild(doc.createTextNode(entry.getPlayerName()));
                entryElement.appendChild(player);

                Element score = doc.createElement("score");
                score.appendChild(doc.createTextNode(String.valueOf(entry.getScore())));
                entryElement.appendChild(score);

                rootElement.appendChild(entryElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
