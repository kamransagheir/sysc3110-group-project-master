package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Handles saving and parsing a board to an XML file.
 */
public class XMLParser {

    /**
     * The logger used to log any errors.
     */
    private static Logger logger = LogManager.getLogger(Board.class);

    /**
     * Writes a board into an XML encoded file.
     * @param board The board being saved.
     * @param fileName The name of the file it is saved to.
     * @throws IOException If the buffered writer fails.
     */
    public static void writeToXMLFile(Board board, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName +
                ".XML"));
        writer.write(board.toXML());
        writer.close();
    }

    /**
     * Returns a BoardItem described by an XML node?
     * @param node The current element.
     * @return The BoardItem contained in the node.
     */
    private static BoardItem itemFromXML(Node node) {
        Coordinate failCoord = new Coordinate(-1, -1);
        BoardItem itemToAdd = new EmptyBoardItem(failCoord);

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            if (!node.getNodeName().equals("Coordinate")) {

                //If it has more than 1 child, its containable item
                if (node.getChildNodes().getLength() != 1) {

                    Optional<Containable> optional =
                            Optional.of(new Rabbit(-1, -1));
                    Coordinate containableCoordinate =
                            XMLParser.coordinateFromXML(node.getChildNodes());

                    for (int i = 0; i < node.getChildNodes().getLength(); i++) {

                        if (node.getChildNodes().item(i).getNodeName().equals("Rabbit")) {
                            Rabbit rabbit = new Rabbit (containableCoordinate);
                            optional = Optional.of(rabbit);
                        }

                        if (node.getChildNodes().item(i).getNodeName().equals("Mushroom")) {
                            Mushroom mushroom =
                                    new Mushroom(containableCoordinate);
                            optional = Optional.of(mushroom);
                        }



                    }

                    if (node.getNodeName().equals("ElevatedBoardItem")) {
                        //if optional is -1, -1 coordinate. This is broken.
                        itemToAdd =
                                new ElevatedBoardItem(containableCoordinate, optional);
                    }

                    if (node.getNodeName().equals("Hole")) {
                        //if optional is -1, -1 coordinate. This is broken.
                        itemToAdd =
                                new Hole(containableCoordinate, optional);
                    }

                } else {
                    //not containable
                    if (node.getNodeName().equals("Fox")) {
                        Pair<Coordinate, Coordinate> coordinates =
                                XMLParser.foxCoordinateFromXML(node.getChildNodes());

                        itemToAdd = new Fox(coordinates);

                    } else {
                        //Only 1 coordinate
                        Coordinate coordinate =
                                XMLParser.coordinateFromXML(node.getChildNodes());

                        if (node.getNodeName().equals("Empty")) {
                            itemToAdd = new EmptyBoardItem(coordinate);
                        }

                        else if (node.getNodeName().equals("Hole")) {
                            itemToAdd = new Hole(coordinate,
                                    Optional.absent());
                        }

                        else if (node.getNodeName().equals("ElevatedBoardItem")) {
                            itemToAdd = new ElevatedBoardItem(coordinate,
                                    Optional.absent());
                        }

                        else if (node.getNodeName().equals("Rabbit")) {
                            itemToAdd = new Rabbit(coordinate);
                        }

                        else if (node.getNodeName().equals("Mushroom")) {
                            itemToAdd = new Mushroom(coordinate);
                        }

                    }

                }

            }

        }

        return itemToAdd;
    }

    /**
     * Returns the pair of coordinates of a fox in an xml file.
     * @param nodeList The two nodes containing the fox.
     * @return The pair of coordinates of the fox.
     */
    private static Pair<Coordinate, Coordinate> foxCoordinateFromXML(NodeList nodeList) {
        int headRow = -1;
        int headColumn = -1;
        int tailRow = -1;
        int tailColumn = -1;

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);

            if (tempNode.getNodeName().equals("CoordinatePair")) {
                // get attributes names and values
                NamedNodeMap nodeMap = tempNode.getAttributes();


                for (int i = 0; i < nodeMap.getLength(); i++) {

                    Node node = nodeMap.item(i);

                    if (node.getNodeName().equals("headRow")) {
                        headRow = Integer.parseInt(node.getNodeValue());
                    }

                    if (node.getNodeName().equals("headColumn")) {
                        headColumn = Integer.parseInt(node.getNodeValue());
                    }

                    if (node.getNodeName().equals("tailRow")) {
                        tailRow = Integer.parseInt(node.getNodeValue());
                    }

                    if (node.getNodeName().equals("tailColumn")) {
                        tailColumn = Integer.parseInt(node.getNodeValue());
                    }

                }
            }
        }

        if (headRow == -1 || headColumn == -1 || tailRow == -1 || tailColumn == -1) {
            Coordinate head = new Coordinate(headRow, headColumn);
            Coordinate tail = new Coordinate(tailRow, tailColumn);
            logger.error("ERROR: " + Pair.pair(head, tail));
            throw new RuntimeException("Getting coordinate is broken!");
        }

        Coordinate head = new Coordinate(headRow, headColumn);
        Coordinate tail = new Coordinate(tailRow, tailColumn);

        return Pair.pair(head, tail);

    }

    /**
     * Returns the coordinate of the current element.
     * @param nodeList The node containing the current element's coordinates.
     * @return The coordinate of the element.
     */
    private static Coordinate coordinateFromXML(NodeList nodeList) {

        int row = -1;
        int column = -1;

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);

            if (tempNode.getNodeName().equals("Coordinate")) {
                // get attributes names and values
                NamedNodeMap nodeMap = tempNode.getAttributes();


                for (int i = 0; i < nodeMap.getLength(); i++) {

                    Node node = nodeMap.item(i);

                    if (node.getNodeName().equals("row")) {
                        row = Integer.parseInt(node.getNodeValue());
                    }

                    if (node.getNodeName().equals("column")) {
                        column = Integer.parseInt(node.getNodeValue());
                    }

                }
            }
        }

        if (row == -1 || column == -1) {
            logger.error(row + " : " + column);
            throw new RuntimeException("Getting coordinate is broken!");
        }

        return new Coordinate(row, column);
    }

    /**
     * Reads a board from a given XML file and returns the board constructed from the file.
     * @param fileName The name of the file containing the board's XML representation.
     * @return The board represented by the xml file.
     * @throws IOException If the document reader fails.
     * @throws ParserConfigurationException If the document parser fails.
     * @throws SAXException If the SAX parser fails.
     */
    public static Board boardFromXML(String fileName) throws IOException, ParserConfigurationException, SAXException {

        //If user did not enter a value, save to file called "default.XML".
        System.out.println("FILENAME: " + fileName);
        if (fileName.equals("")) {
            System.out.println(fileName);
            fileName = "default";
        }

        File file = new File(fileName + ".XML");

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();

        Document doc = dBuilder.parse(file);

        Board board = new Board(5, 5);

        NodeList boardElements = doc.getDocumentElement().getChildNodes();
        for (int count = 0; count < boardElements.getLength(); count++) {
            //loop through the items and add them to the board
            board =
                    board.setItem(XMLParser.itemFromXML(boardElements.item(count)));
        }

        return board;
    }
}
