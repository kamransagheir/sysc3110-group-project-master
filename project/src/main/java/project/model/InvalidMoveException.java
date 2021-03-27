package project.model;

/**
 * An exception thrown if an attempted move is invalid.
 */
public class InvalidMoveException extends Exception {
    InvalidMoveException(String s) {
        super(s);
    }

}
