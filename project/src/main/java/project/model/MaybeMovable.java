package project.model;

/**
 * Represents objects that maybe can move on the board
 */
public interface MaybeMovable {
    /**
     * Returns true of the object is a movable (A rabbit in a hole is movable).
     * @return True if the object is a movable.
     */
    boolean isMovable();
}
