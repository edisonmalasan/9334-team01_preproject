package Server.model;

import common.model.QuestionModel;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLStorageModel {
    public static void loadQuestionFromXML(String filePath, QuestionBankModel questionBank) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList questionList = document.getElementsByTagName("question");

            for (int i = 0; i < questionList.getLength(); i++) {
                Node questionNode = questionList.item(i);

                if (questionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) questionNode;
                    String category = element.getElementsByTagName("category").item(0).getTextContent();
                    String text = element.getElementsByTagName("text").item(0).getTextContent();
                    String answer = element.getElementsByTagName("answer").item(0).getTextContent();

                    questionBank.add(new QuestionModel(category, text, answer));
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}