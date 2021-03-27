package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Either;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcollections.PMap;
import project.tui.ItemUIRepresentation;

import java.util.HashSet;

public class Rabbit extends SingleBoardItem implements Containable, Movable {

    private static Logger logger = LogManager.getLogger(Board.class);

    /**
     * Constructor for a rabbit initializes uirepresentation and sets
     * coordinates using coordinate parameter.
     * @param coordinate coordinate used where rabbit will be placed.
     */
    public Rabbit(Coordinate coordinate) {
        super(coordinate);
        this.uIRepresentation = ItemUIRepresentation.RABBIT;
    }

    /**
     * Constructor for a rabbit that initializes ui representation and sets
     * coordinates using row and column parameters.
     * @param row
     * @param column
     */
    public Rabbit (int row, int column) {
        super(new Coordinate(row, column));
        this.uIRepresentation = ItemUIRepresentation.RABBIT;
    }

    /**
     * Computes next coordinate using the to be checked when jumping.
     * @param direction the direction the rabbit is jumping in.
     * @return new Coordinate based on what direction its jumping.
     */
    private Coordinate computeCoordinateFromDirection(Direction direction) {

        Coordinate current = this.coordinate.left().get();

        switch (direction) {
            case RIGHT:
                return new Coordinate(current.row, current.column + 1);
            case LEFT:
                return new Coordinate(current.row, current.column - 1);
            case DOWN:
                return new Coordinate(current.row + 1, current.column);
            case UP:
                return new Coordinate(current.row - 1, current.column);
            default:
                throw new IllegalArgumentException("Invalid Direction.");
        }
    }

    /**
     * Makes a rabbit jump by delegating to another jump method.
     * @param direction direction the rabbit is jumping in.
     * @param slice slice of the board to be checked when performing the jump.
     * @return calls the other jump method and returns what that jump method
     * will return.
     * @throws InvalidMoveException
     */
    public Either<Rabbit, ContainerItem> jump(Direction direction,
                                              PMap<Coordinate, BoardItem> slice) throws InvalidMoveException {
       return jump(direction, slice, false);
    }

    /**
     * Makes a rabbit jump by using checks if rabbit is currently jumping.
     * @param direction direction the rabbit is jumping in.
     * @param slice slice of the board to be checked when performing the jump.
     * @param isCurrentlyJumping is a boolean to determine if the rabbit is
     *                           currently jumping.
     * @return returns updated rabbit with new coordinates.
     * @throws InvalidMoveException
     */
    private Either<Rabbit, ContainerItem> jump(Direction direction,
                                               PMap<Coordinate, BoardItem> slice
            , boolean isCurrentlyJumping) throws InvalidMoveException {
        Coordinate coordinate = computeCoordinateFromDirection(direction);

        if (checkIfNotOnBoard(slice, coordinate)) {
            throw new InvalidMoveException("Jumping caused Rabbit to fall off" +
                    " board");
        }

        Rabbit jumpingRabbit = new Rabbit(coordinate);

        BoardItem item = slice.get(coordinate);

        // Found obstacle
        //Perform Jump
        if (item.isObstacle()){
            return jumpingRabbit.jump(direction, slice, true);
        }

        // Could be empty hole or empty item

        // R M E  ==> obstacle found, keep going,

        // R E E  => error

        // R M H => E M H(R)

        // Not found obstacle
        if (isCurrentlyJumping) {

            if (item instanceof ContainerItem) {
                ContainerItem newContainerItem;
                if (item instanceof Hole) {
                    newContainerItem = new Hole(coordinate, Optional.of(jumpingRabbit));
                } else {
                    newContainerItem = new ElevatedBoardItem(coordinate,
                            Optional.of(jumpingRabbit));
                }
                return Either.right(newContainerItem);
            } else {
                return Either.left(jumpingRabbit);
            }
        } else {
            throw new InvalidMoveException("Cannot move without obstacles");
        }
    }

    /**
     * check if rabbit fell off the board while jumping.
     * @param slice slice of the board to check for the jump.
     * @param nextCoordinates the coordinates the rabbit is jumping to.
     * @return boolean depending on if the rabbit is on the board or not,
     * false if the rabbit is on the board, true otherwise.
     */
    private boolean checkIfNotOnBoard(
            PMap<Coordinate, BoardItem> slice, Coordinate nextCoordinates
    ) {
        HashSet<Coordinate> coordinateSet = new HashSet<>(slice.keySet());

        if (!coordinateSet.contains(nextCoordinates)) {
            return true;
        }

        return false;
    }

    /**
     * Method used to check if this Rabbit object is an obstacle.
     * @return true All Rabbits are obstacles.
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
        logger.trace("Checking rabbit!");
        if (this == o) {return true;}

        if (o == null) {return false;}

        if (this.getClass() != o.getClass()) {return false;}

        Rabbit rabbit = (Rabbit) o;

        if (rabbit.coordinate.left().get().column ==
                this.coordinate.left().get().column) {

            if (rabbit.coordinate.left().get().row ==
                    this.coordinate.left().get().row) {
                logger.trace("Rabbit IS SAME!");
                return true;
            }

        }

        return false;
    }

    /**
     * Will return the XML representation of this rabbit.
     * @return the XML representation of the rabbit.
     */
    @Override
    public String toXML() {
        String xml = "<Rabbit>";
        Coordinate coordinate = this.coordinate.left().get();

        xml = xml + "<Coordinate row=" + '"' + coordinate.row + '"' +
                " column=" + '"' + coordinate.column + '"' + "/>";

        xml = xml + "</Rabbit>";
        return xml;
    }
}
