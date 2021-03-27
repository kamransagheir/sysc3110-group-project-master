package project.view;
import project.model.Coordinate;

/**
 * An event thrown by objects when they are clicked, specifying their coordinates.
 */
public class ItemClickEvent {
    public Coordinate coordinate;

    /**
     * Creates the click event, storing the item's coordinates.
     * @param itemCoordinate The coordinates of the item that sent the event.
     */
    public ItemClickEvent(Coordinate itemCoordinate) {
        this.coordinate = itemCoordinate;
    }

}
