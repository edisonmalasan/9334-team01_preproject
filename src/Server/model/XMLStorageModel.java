package Server.model;

import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import Client.model.PlayerModel;
import common.model.QuestionModel;
import org.w3c.dom.*;
import java.io.*;

public class XMLStorageModel {

    public static List<QuestionModel> loadQuestionsFromXML(String filename) {
        List<QuestionModel> questions = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) return questions;

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("question");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String category = element.getAttribute("category");
                String questionText = element.getElementsByTagName("text").item(0).getTextContent();
                String correctAnswer = element.getElementsByTagName("answer").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());
                List<String> choices = new ArrayList<>();

                NodeList choiceNodes = element.getElementsByTagName("choice");
                for (int j = 0; j < choiceNodes.getLength(); j++) {
                    choices.add(choiceNodes.item(j).getTextContent());
                }

                questions.add(new QuestionModel(category, questionText, choices, correctAnswer, score)); // 5 pass
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
    public static List<LeaderboardEntryModelServer> loadLeaderboardFromXML(String filename) {
        List<LeaderboardEntryModelServer> leaderboard = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Leaderboard file not found. Creating a new one.");
                file.createNewFile(); // create file if it doesnt exist
                return leaderboard; // return empty list
            }

            // if file is empty
            if (file.length() == 0) {
                System.out.println("Leaderboard file is empty. Initializing with an empty leaderboard.");
                return leaderboard; // return empty list
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String playerName = element.getElementsByTagName("player").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());
                PlayerModel newPlayer = new PlayerModel(playerName,score);
                leaderboard.add(new LeaderboardEntryModelServer(playerName, score));
            }
        } catch (Exception e) {
            System.err.println("Error loading leaderboard from XML: " + e.getMessage());
            e.printStackTrace();
        }
        return leaderboard;
    }

    public static void saveLeaderboardToXML(String filename, List<LeaderboardEntryModelServer> leaderboard) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("leaderboard");
            doc.appendChild(rootElement);

            for (LeaderboardEntryModelServer entry : leaderboard) {
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
