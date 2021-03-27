package project.model;

import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcollections.PMap;
import project.tui.ItemUIRepresentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A class that represents a fox on the board, which can slide across the board to move.
 */
public class Fox extends BoardItem implements Movable {

    private static Logger logger = LogManager.getLogger(Fox.class);

    /**
     * The orientation of the fox
     */
    public final Orientation orientation;

    /**
     * Ensures that given head and tail coordinates do not conflict with each other
     * @param coords The pair of coordinates of the Fox.
     */
    private static void validateArguments(Pair<Coordinate, Coordinate> coords) {
        validateArguments(coords.left().row, coords.left().column,
                coords.right().row, coords.right().column);
    }

    /**
     * Ensures that the head and tail do not conflict with each other.
     * @param headRow The row of the fox's head.
     * @param headColumn The column of the fox's head.
     * @param tailRow The row of the fox's tail.
     * @param tailColumn The column of the fox's tail.
     * @throws IllegalArgumentException If the hea and tail conflict with eachother.
     */
    private static void validateArguments(int headRow, int headColumn,
                                          int tailRow, int tailColumn)
            throws IllegalArgumentException {

        if (headColumn == tailColumn && headRow == tailRow) {
            throw new IllegalArgumentException("The fox cannot have its tail " +
                    "and head in the same position");
        }

        if (Math.abs(headColumn - tailColumn) > 1 ||
                Math.abs(headRow - tailRow) > 1) {
            throw new IllegalArgumentException(
                    "The fox cannot have its tail more than a unit " +
                            "a way from its head");
        }

        if (Math.abs(headColumn - tailColumn) == Math.abs(headRow - tailRow)) {
            throw new IllegalArgumentException("The fox cannot have its tail " +
                    "diagonal to its head");
        }
    }

    /**
     * Ensures that given head and tail coordinates do not conflict with each other
     * @param coordinates The coordinates of the head and tail.
     */
    public Fox(Pair<Coordinate, Coordinate> coordinates) {
        super(coordinates);
        if (coordinates.left().row - coordinates.right().row != 0) {
            //should be vertical if row is not same for head and tail
            this.orientation = Orientation.VERTICAL;

        }

        else {
            this.orientation = Orientation.HORIZONTAL;
        }
        this.uIRepresentation = ItemUIRepresentation.FOX;
        validateArguments(coordinates);
    }

    /**
     * Method used to determine what next coordinate should be checked when sliding the fox
     * @return nextCoordinates The next coordinates that should be checked when sliding the fox
     * @throws InvalidMoveException
     */
    private Pair<Coordinate, Coordinate> computeNextCoordinates(Direction direction)
            throws InvalidMoveException {
        Coordinate head = getHead();
        Coordinate tail = getTail();

        if (this.orientation == Orientation.HORIZONTAL) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                throw new InvalidMoveException("Fox is oriented horizontally!");
            }
        } else if (this.orientation == Orientation.VERTICAL) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                throw new InvalidMoveException("Fox is oriented vertically!");
            }
        }

        if (direction == Direction.DOWN) {
            Coordinate newHead = new Coordinate(head.row + 1, head.column);
            Coordinate newTail = new Coordinate(tail.row + 1, tail.column);
            return Pair.pair(newHead, newTail);
        } else if (direction == Direction.UP) {
            Coordinate newHead = new Coordinate(head.row - 1, head.column);
            Coordinate newTail = new Coordinate(tail.row - 1, tail.column);
            return Pair.pair(newHead, newTail);
        } else if (direction == Direction.RIGHT) {
            Coordinate newHead = new Coordinate(head.row, head.column + 1);
            Coordinate newTail = new Coordinate(tail.row, tail.column + 1);
            return Pair.pair(newHead, newTail);
        } else if (direction == Direction.LEFT) {
            Coordinate newHead = new Coordinate(head.row, head.column - 1);
            Coordinate newTail = new Coordinate(tail.row, tail.column - 1);
            return Pair.pair(newHead, newTail);
        }

        throw new IllegalArgumentException("Invalid Direction!");
    }

    /**
     * Checks which slide should be used
     * @param slice A slice of the board used to get the item at a given coordinate
     * @param moveSpaces The spaces the Fox wants to move
     * @return slidingFox A new Fox at the destinationCoordinate or at same location if the slide failed
     */
    public Fox slide(PMap<Coordinate, BoardItem> slice, int moveSpaces,
                     Direction direction) throws InvalidMoveException {

        return performSlide(slice, moveSpaces, direction);
    }

    /**
     * Checks if sliding the Fox horizontally to the destination coordinate is valid
     * @param slice A slice of the board used to get the item at a given coordinate
     * @param moveSpaces The spaces the Fox wants to move
     * @return slidingFox A new Fox at the destinationCoordinate or at same location if the slide failed
     */
    public Fox performSlide(PMap<Coordinate, BoardItem> slice, int moveSpaces, Direction direction)
            throws InvalidMoveException {
        // Generate new coordinates
        Pair<Coordinate, Coordinate> nextCoordinates =
                this.computeNextCoordinates(direction);

        int spacesToMove = moveSpaces;

        // Create new Fox
        Fox fox = new Fox(nextCoordinates);

        List<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(nextCoordinates.left());
        coordinates.add(nextCoordinates.right());

        if (checkIfNotOnBoard(slice, coordinates)) {
            throw new InvalidMoveException("Slide caused fox to fall off " +
                    "board");
        }

        if (checkIfHitObstacle(slice, coordinates)) {
            throw new InvalidMoveException("Slide caused fox to hit an " +
                    "obstacle");
        }

        if (spacesToMove > 1) {
            return fox.performSlide(slice, --spacesToMove, direction);
        }

        return fox;
    }

    /**
     * Used to determine if fox slide hit an obstacle or not.
     * @param slice sends in slice of the board to loop through.
     * @param coordinates gives list of coordinates holding the coordinates
     * of the fox.
     * @return boolean is returned. True if it hit and obstacle, otherwise
     * false.
     */
    private boolean checkIfHitObstacle(PMap<Coordinate, BoardItem> slice,
                                       List<Coordinate> coordinates ) {
        for (Coordinate coordinate: coordinates) {

            BoardItem item = slice.get(coordinate);

            if (item.isObstacle() && !item.equals(this)) {
                return true;
            }

            if (item instanceof ContainerItem) {
                return true;
            }
        }

        return false;
    }

    /**
     * Used to determine if slide caused fox to fall off the board.
     * @param slice sends in slice of the board to loop through.
     * @param nextCoordinates gives list of coordinates holding the
     *                        coordinates of the fox.
     * @return boolean is returned. True if it fell off the board, otherwise
     * false is returned.
     */
    private boolean checkIfNotOnBoard(
            PMap<Coordinate, BoardItem> slice, List<Coordinate> nextCoordinates
    ) {
        HashSet<Coordinate> coordinateSet = new HashSet<>(slice.keySet());

        if (!coordinateSet.containsAll(nextCoordinates)) {
            return true;
        }

        return false;
    }


    /**
     * Returns whether this object can be treated as an obstacle
     * @return true The Fox object is an obstacle
     */
    @Override
    public boolean isObstacle() {
        return true;
    }

    /**
     * gets tail coordinate for the fox.
     * @return coordinate.right().get().right().
     */
    public Coordinate getTail() {
       return coordinate.right().get().right();
    }

    /**
     * gets head coordinate for the fox.
     * @return coordinate.right().get().left().
     */
    public Coordinate getHead() {
        return coordinate.right().get().left();
    }

    /**
     * equals method for Fox.
     * @param o The object being compared.
     * @return true or false based on if its equals
     */
    @Override
    public boolean equals(Object o) {
        logger.trace("Checking fox!");
        if (this == o) {return true;}

        if (o == null) {return false;}

        if (this.getClass() != o.getClass()) {return false;}

        Fox fox = (Fox) o;

        if ((fox.coordinate.right().get().left().column ==
                this.coordinate.right().get().left().column) &&
                fox.coordinate.right().get().left().row ==
                        this.coordinate.right().get().left().row) {

            if ((fox.coordinate.right().get().right().row ==
                    this.coordinate.right().get().right().row &&
                    fox.coordinate.right().get().right().column ==
                            this.coordinate.right().get().right().column)) {
                logger.trace("Fox IS SAME!");
                return true;
            }

        }

        return false;
    }

    /**
     * Will return the XML representation of this fox.
     * @return the XML representation of the fox.
     */
    @Override
    public String toXML() {
        //<CoordinatePair headRow=0 headColumn=0 tailRow=1 tailColumn=1/>
        String xml = "<Fox>";
        Pair<Coordinate, Coordinate> coordinates = this.coordinate.right().get();

        xml = xml + "<CoordinatePair headRow=" + '"' + coordinates.left().row + '"' +
                " headColumn=" + '"' + coordinates.left().column + '"' + " tailRow=" + '"' +
                coordinates.right().row + '"' + " tailColumn=" + '"' +
                coordinates.right().column + '"' + "/>";

        xml = xml + "</Fox>";
        return xml;
    }
}
