package project.model;

/**
 * Represents objects that may be obstacles on the board.
 */
public interface MaybeObstacle {
    /**
     * Returns true of the object is an obstacle (A rabbit could jump over it).
     * @return True if the object is an obstacle.
     */
    boolean isObstacle();

}
