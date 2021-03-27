package project.solver;

import project.model.Direction;
import project.model.BoardItem;
import project.model.Coordinate;

/**
 * Used to store valid moves for a particular object.
 * These valid moves are used in the solver code.
 */
public class Move {

    /**
     * The item to generate the move for.
     */
    public final BoardItem item;

    /**
     * The coordinate of the item before the move is completed.
     */
    public final Coordinate initial;

    /**
     * The coordinate of the item after the move is finished.
     */
    public final Coordinate ending;

    /**
     * The direction the item will move.
     */
    public final Direction direction;

    /**
     * Constructor used to create the move.
     * @param item to perform the move.
     * @param direction the item will be moving.
     * @param initial coordinate the item is before moving.
     * @param ending coordinate the item will be after moving.
     */
    public Move(BoardItem item, Direction direction, Coordinate initial,
                Coordinate ending) {

        // Debug info
        this.item = item;
        this.direction = direction;

        this.initial = initial;
        this.ending = ending;
    }

    /**
     * Equals method used to determine if the moves are equal.
     * @param o object move to compare to this move.
     * @return true or false based on if the moves are equal.
     */
    @Override
    public boolean equals(Object o) {
        
        if (this == o) return true;

        if (o == null) return false;

        if (this.getClass() != o.getClass())
            return false;

        Move move = (Move) o;


        return (item.equals(move.item) &&
                initial.equals(move.initial) &&
                direction.equals(move.direction) &&
                ending.equals(move.ending));

    }

    /**
     * toString method returns the move object in a string format.
     * @return str, the string representation of the move.
     */
    @Override
    public String toString()  {
        String str = "";
        str += "Item: " + item.getClass() + " from: " + initial + " to: " + ending;

        return str;
    }

}
