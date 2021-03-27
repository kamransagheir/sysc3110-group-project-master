package project.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.tui.ItemUIRepresentation;

/**
 * Represents a mushroom item on the board.
 */
public final class Mushroom extends SingleBoardItem implements Containable {

    /**
     * The logger used to log errors.
     */
    private static Logger logger = LogManager.getLogger(Board.class);

    /**
     * Creates a mushroom at a specific row and column.
     * @param row The row where the mushroom is located.
     * @param column The colum where the mushroom is located.
     */
    public Mushroom(int row, int column) {
        super(new Coordinate(row, column));
    }

    /**
     * Creates a mushroom at a specific coordinate.
     * @param coordinate The coordinate where the mushroom is located.
     */
    public Mushroom(Coordinate coordinate) {
        super(coordinate);
        this.uIRepresentation = ItemUIRepresentation.MUSHROOM;
    }

    /**
     * Returns true, since mushrooms are obstacles.
     * @return True.
     */
    @Override
    public boolean isObstacle() {
        return true;
    }

    /**
     * Checks if this object is equal to another.
     * @param o The object being compared.
     * @return True if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        logger.trace("Checking mushroom!");
        if (this == o) {return true;}

        if (o == null) {return false;}

        if (this.getClass() != o.getClass()) {return false;}

        Mushroom mushroom = (Mushroom) o;

        if (mushroom.coordinate.left().get().column ==
                this.coordinate.left().get().column) {

            if (mushroom.coordinate.left().get().row ==
                    this.coordinate.left().get().row) {
                logger.trace("Mushroom IS SAME!");
                return true;
            }

        }

        return false;
    }

    /**
     * Will return the XML representation of this mushroom.
     * @return the XML representation of the mushroom.
     */
    @Override
    public String toXML() {
        String xml = "<Mushroom>";
        Coordinate coordinate = this.coordinate.left().get();

        xml = xml + "<Coordinate row=" + '"' + coordinate.row + '"' +
                " column=" + '"' + coordinate.column + '"' + "/>";

        xml = xml + "</Mushroom>";
        return xml;
    }
}
