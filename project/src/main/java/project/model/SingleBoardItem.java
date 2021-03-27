package project.model;

/**
 * Specifies a board item with a single coordinate.
 */
public abstract class SingleBoardItem extends BoardItem{

    /**
     * Creates a single board item at a coordinate.
     * @param coordinate The coordinate containing the board item.
     */
    public SingleBoardItem(Coordinate coordinate) {
        super(coordinate);
    }
}
