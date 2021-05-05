package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.leader.LeaderCard;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Deprecated(forRemoval = true)
public final class XMLParser {
    private static final boolean DEBUG_MODE = true;
    private static final boolean BEST_EFFORT_MODE = false;

    private XMLParser() { }

    private static Document preliminaryOperations(String fileName) throws FileNotFoundException, ParserException {
        File file;
        DocumentBuilder builder;
        Document doc;

        // test for null or non existing file
        if(fileName == null)
            throw new NullPointerException();

        file = new File(fileName);
        if(!file.exists())
            throw new FileNotFoundException("File " + fileName + " not found");

        // create builder and parse file
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new UnknownError(e.getMessage());
        }

        try {
            doc = builder.parse(file);
        } catch (SAXException | IOException e) {
            throw new ParserException(e.getMessage());
        }

        // normalize file
        doc.getDocumentElement().normalize();

        return doc;
    }

    private static boolean isElementNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE;
    }

    private static boolean isTextNode(Node node) {
        return node.getNodeType() == Node.TEXT_NODE;
    }

    private static void errorHandler(String message) throws ParserException {
        if(BEST_EFFORT_MODE)
            Logger.log(message, Logger.Severity.WARNING, Logger.Format.YELLOW);
        else
            throw new ParserException(message);
    }

    public static List<LeaderCard> parseLeaders(String fileName) throws FileNotFoundException, ParserException {
        ArrayList<LeaderCard> leaders = new ArrayList<>();
        NodeList cards, card, attributes, requirements, special_abilities;
        Set<Integer> ids = new HashSet<>();

        int id, points;
        String name;

        boolean abort;

        // PRELIMINARY OPERATIONS
        Document doc = preliminaryOperations(fileName);

        // START BUILDING
        cards = doc.getElementsByTagName("card");

        if(cards.getLength() == 0)
            throw new ParserException("Couldn't parse any cards in the " + fileName + " file");

        // For each card
        for(int i = 0; i < cards.getLength(); i++) {

            // check if a node is an element node
            if(isElementNode(cards.item(i))) {

                if (DEBUG_MODE)
                    Logger.log("---------- CARD " + i + " ----------");

                // get all properties
                card = cards.item(i).getChildNodes();

                // Reset all
                name = null;
                id = -1;
                points = -1;
                requirements = null;
                special_abilities = null;

                abort = false;

                // For each property
                for (int j = 0; j < card.getLength() && !abort; j++) {

                    // check if a node is an element node
                    if (isElementNode(card.item(j))) {

                        // Get all values
                        switch (card.item(j).getNodeName().toLowerCase()) {
                            case "id":
                                try {
                                    id = Integer.parseInt(card.item(j).getTextContent());
                                } catch (NumberFormatException e) {
                                    errorHandler("Unable to parse integer field \"id\" from " + card.item(j).getTextContent());
                                    abort = true;
                                }
                                break;

                            case "name":
                                if (card.item(j).getTextContent().isEmpty()) {
                                    errorHandler("Unable to parse string field \"name\"");
                                    abort = true;
                                }
                                else
                                    name = card.item(j).getTextContent().trim();
                                break;

                            case "points":
                                try {
                                    points = Integer.parseInt(card.item(j).getTextContent());
                                } catch (NumberFormatException e) {
                                    errorHandler("Unable to parse integer field \"points\" from " + card.item(j).getTextContent());
                                    abort = true;
                                }
                                break;

                            case "requirements":
                                //TODO: to  be implemented
                                requirements = card.item(j).getChildNodes();
                                break;

                            case "special_abilities":
                                //TODO: to be implemented
                                break;
                        }
                    }
                }

                // if parsing any of the properties failed, skip assign
                if(abort)
                    continue;

                // check validity of all fields
                if (id <= 0) {
                    errorHandler("Invalid or absent property \"id\"");
                    continue;
                }

                if (ids.contains(id)) {
                    errorHandler("Duplicate card property \"id\" " + id);
                    continue;
                } else
                    ids.add(id);

                if (points < 0) {
                    errorHandler("Invalid or absent property \"points\" of card " + id);
                    continue;
                }

                if (DEBUG_MODE) {
                    Logger.log("Id: " + id);
                    Logger.log("Name: " + name);
                    Logger.log("Points: " + points);
                }
            }
        }

        return new ArrayList<>();
    }
}
