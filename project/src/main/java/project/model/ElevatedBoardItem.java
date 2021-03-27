package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcollections.PMap;
import project.tui.ItemUIRepresentation;

/**
 * A space on the board that is elevated.
 */
public class ElevatedBoardItem extends ContainerItem {

    /**
     * The logger used to log errors.
     */
    private static Logger logger = LogManager.getLogger(Board.class);

    /**
     * Constructs a new hole with a coordinate and optional item.
     *
     * @param coordinate     The coordinate where the hole is located.
     * @param containingItem The optional item that it can contain.
     */
    public ElevatedBoardItem(Coordinate coordinate, Optional<Containable> containingItem) {
        super(coordinate, containingItem);
        if (containingItem.isPresent()) {
            if (containingItem.get() instanceof Rabbit) {
                this.uIRepresentation =
                        ItemUIRepresentation.ELEVATED_RABBIT;
            }
            else if (containingItem.get() instanceof Mushroom) {
                this.uIRepresentation = ItemUIRepresentation.ELEVATED_MUSHROOM;
            }
        }

        else {
            this.uIRepresentation = ItemUIRepresentation.ELEVATED;
        }
    }

    public ElevatedBoardItem(Coordinate coordinate) {
        this(coordinate, Optional.absent());
    }

    /**
     * Attempts to jump a board out of the elevated space.
     * @param direction The direction that the rabbit must jump.
     * @param slice The slice where the jump occurs.
     * @return A pair containing the container and the result of the jump.
     * @throws InvalidMoveException If the move is invalid.
     */
    @Override
    public Pair<ContainerItem, Either<Rabbit, ContainerItem>> jump(Direction direction, PMap<Coordinate, BoardItem> slice) throws InvalidMoveException {
        if (!containingItem.isPresent()){
            if (! (containingItem.get() instanceof  Rabbit)) {
                throw new InvalidMoveException("The hole does not contain a rabbit.");
            }
        }

        //Jump the rabbit out of the hole
        Rabbit jumpingRabbit = (Rabbit) this.containingItem.get();
        Either<Rabbit, ContainerItem> rabbitOrContainerItem = jumpingRabbit.jump(direction, slice);

        //Create a new empty hole in this one's place
        ElevatedBoardItem emptyContainerItem = new ElevatedBoardItem(this.coordinate.left().get(), Optional.absent());

        return Pair.pair(emptyContainerItem, rabbitOrContainerItem);
    }

    /**
     * Returns true of this object is the same as another ElevatedBoardItem.
     * @param o The object being compared.
     * @return True if they are the same.
     */
    @Override
    public boolean equals(Object o) {
        logger.trace("Checking elevated!");
        if (this == o) {return true;}

        if (o == null) {return false;}

        if (this.getClass() != o.getClass()) {return false;}

        ElevatedBoardItem elevatedBoardItem = (ElevatedBoardItem) o;

        if (elevatedBoardItem.coordinate.left().get().column ==
                this.coordinate.left().get().column) {

            if (elevatedBoardItem.coordinate.left().get().row ==
                    this.coordinate.left().get().row) {

                if (elevatedBoardItem.containingItem.isPresent() &&
                        this.containingItem.isPresent()) {

                    if (elevatedBoardItem.containingItem.get().getClass() ==
                            this.containingItem.get().getClass()) {
                        logger.trace("Mushroom IS SAME!");
                        return true;
                    }

                }

                else if (!elevatedBoardItem.containingItem.isPresent() &&
                        !elevatedBoardItem.containingItem.isPresent()) {
                    logger.trace("Elevated IS SAME!");
                    return true;
                }

            }

        }

        return false;
    }

    /**
     * Will return the XML representation of this elevated board item and
     * anyy items elevated by this item.
     * @return the XML representation of the elevated board item.
     */
    @Override
    public String toXML() {
        String xml = "<ElevatedBoardItem>";
        Coordinate coordinate = this.coordinate.left().get();

        xml = xml + "<Coordinate row=" + '"' + coordinate.row + '"' + " column=" +
                '"' + coordinate.column + '"' + "/>";

        if (containingItem.isPresent()) {
            BoardItem item = (BoardItem) containingItem.get();
            xml = xml + item.toXML();
        }

        xml = xml + "</ElevatedBoardItem>";
        return xml;
    }
}
