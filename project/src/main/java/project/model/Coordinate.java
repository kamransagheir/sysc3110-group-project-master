package project.model;

import java.util.Objects;

/**
 * Represents a coordinate on the board, consisting of the item's row and column.
 */
public class Coordinate {
    /**
     * The row of the coordinate.
     */
    public final int row;
    /**
     * The column of the coordinate.
     */
    public final int column;

    /**
     * Creates a coordinate with a given row and column.
     * @param row The row of the coordinate.
     * @param column The column of the coordinate.
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Creates a copy of a coordinate using another coordinate.
     * @param coordinate The coordinate being copied.
     */
    public Coordinate(Coordinate coordinate) {
        this.row = coordinate.row;
        this.column = coordinate.column;
    }

    /**
     * Returns the hashcode of the coordinate.
     * @return The coordinate's hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * Determines if this coordinate is equal to another.
     * @param o The object being compared to.
     * @return True if they are the same.
     */
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (this.getClass() != o.getClass())
            return false;

        Coordinate coordinate = (Coordinate) o;

        return (this.row == coordinate.row &&
                this.column == coordinate.column);
    }

    /**
     * Returns a string representation of the coordinate.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return row + ":" + column;
    }

}
