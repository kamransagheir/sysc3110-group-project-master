package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import org.pcollections.PMap;

/**
 * Represents a hole object on the board.
 */
public abstract class ContainerItem extends SingleBoardItem implements
        MaybeMovable {
    /**
     * The item that the hole may contain.
     */
    public final Optional<Containable> containingItem;

    /**
     * Constructs a new hole with a coordinate and optional item.
     * @param coordinate The coordinate where the hole is located.
     * @param containingItem The optional item that it can contain.
     */
    protected ContainerItem(Coordinate coordinate, Optional<Containable> containingItem) {
        super(coordinate);
        this.containingItem = containingItem;
    }

    /**
     * Returns true if the hole acts as an obstacle.
     * @return True if the hole contains an item.
     */
    @Override
    public boolean isObstacle() {
        if (containingItem.isPresent()) {
            if (containingItem.get() instanceof MaybeObstacle) {
                return ((MaybeObstacle) containingItem.get()).isObstacle();
            }
        }
        return false;
    }

    /**
     * Returns true if a movable item is contained
     * @return true if the object inside ContainerItem is movable
     */
    @Override
    public boolean isMovable() {
        if (containingItem.isPresent()) {
            if (containingItem.get() instanceof Movable) {
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to jump a rabbit out of a hole.
     * @param direction The direction that the rabbit must jump.
     * @param slice The slice where the jump occurs.
     * @return A pair containing the new empty hole, and either the rabbit or the new hole it is found in.
     * @throws InvalidMoveException If the hole is empty or the rabbit cannot jump.
     */
    public abstract Pair<ContainerItem, Either<Rabbit, ContainerItem>> jump(Direction direction, PMap<Coordinate, BoardItem> slice) throws InvalidMoveException;
}
