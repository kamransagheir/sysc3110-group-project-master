package project.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.tui.ItemUIRepresentation;

/**
 * Represents an empty space on the board.
 */
public class EmptyBoardItem extends SingleBoardItem {

    /**
     * The logger used to log errors.
     */
    private static Logger logger = LogManager.getLogger(Board.class);

    /**
     * Creates the empty board item at a coordinate.
     * @param coordinate The coordinate containing the empty space.
     */
    public EmptyBoardItem(Coordinate coordinate) {
        super(coordinate);
        this.uIRepresentation = ItemUIRepresentation.EMPTY;
    }

    /**
     * Returns the string representation of the board item.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return ItemUIRepresentation.EMPTY.getRepresentation();
    }

    /**
     * This item is not an obstacle, so it returns false.
     * @return False.
     */
    @Override
    public boolean isObstacle() {
        return false;
    }

    /**
     * Compares this item to another to determine if they are equal.
     * @param o The object being compared.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        logger.trace("Checking empty!");
        if (this == o) {return true;}

        if (o == null) {return false;}

        if (this.getClass() != o.getClass()) {return false;}

        EmptyBoardItem empty = (EmptyBoardItem) o;

        if (empty.coordinate.left().get().column ==
                this.coordinate.left().get().column) {

            if (empty.coordinate.left().get().row ==
                    this.coordinate.left().get().row) {
                logger.trace("Empty IS SAME!");
                return true;
            }

        }

        return false;
    }

    /**
     * Will return the XML representation of this empty tile.
     * @return the XML representation of the empty tile.
     */
    @Override
    public String toXML() {
        String xml = "<Empty>";
        Coordinate coordinate = this.coordinate.left().get();

        xml = xml + "<Coordinate row=" + '"' + coordinate.row + '"' +
                " column=" + '"' + coordinate.column + '"' + "/>";

        xml = xml + "</Empty>";
        return xml;
    }
}
