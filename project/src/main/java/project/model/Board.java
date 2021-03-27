package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;




import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;

/**
 * Creates and manages the state of the board by performing operations through
 * various methods.
 * Has methods to delegate slide and jump commands to the animals on the board.
 */
public class Board {

	/**
	 * The number of rows in the board.
	 */
	public final int numberOfRows;

	/**
	 * The number of columns in the board.
	 */
	public final int numberOfColumns;

	/**
	 * The logger used to log any errors.
	 */
	private static Logger logger = LogManager.getLogger(Board.class);

	/**
	 * The persistent map containing the items in the board.
	 */
	private PMap<Coordinate, BoardItem> items;

	/**
	 * The current gamestate of the board, either won or in progress.
	 */
	public final GameState currentGameState;

	/**
	 * Gets the correct slice of the board based on direction
	 *
	 * @param direction  passes in direction the move is to be performed in
	 * @param coordinate coordinate of what is trying ot move
	 * @return rowSlice or columnSlice depending on direction
	 */
	private PMap<Coordinate, BoardItem> getSlice(Direction direction,
												 Coordinate coordinate) {
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			return this.getRowSlice(coordinate.row);
		} else {
			return this.getColumnSlice(coordinate.column);
		}
	}

	/**
	 * Creates a board with a specified number of rows and columns.
	 * @param rows The number of rows the board has.
	 * @param columns The number of columns the board has.
	 */
	public Board(int rows, int columns) {
		this(rows, columns, GameState.IN_PROGRESS);
	}

	/**
	 * Creates a board with a specified number of rows and columns, and a specific gamestate.
	 * @param rows The number of rows the board has.
	 * @param columns The number of columns the board has.
	 * @param gameState The gamestate the board is in.
	 */
	public Board(int rows, int columns, GameState gameState) {
		this.currentGameState = gameState;
		items = HashTreePMap.empty();
		this.numberOfRows = rows;
		this.numberOfColumns = columns;

		// Initialize Board Items
		for (int row = 0; row < numberOfRows; row++) {
			for (int column = 0; column < numberOfColumns; column++) {
				Coordinate currentCoordinate = new Coordinate(row, column);
				BoardItem itemToAdd = new EmptyBoardItem(currentCoordinate);
				items = items.plus(currentCoordinate, itemToAdd);
			}
		}
	}

	/**
	 * Creates a new board using another board.
	 * @param board The board that should be copied.
	 */
	public Board(Board board) {
		this.currentGameState = GameState.IN_PROGRESS;
		this.numberOfRows = board.numberOfRows;
		this.numberOfColumns = board.numberOfColumns;

		this.items = board.items;
	}

	/**
	 * Creates a board using another board and a gamestate.
	 * @param board The board that should be copied.
	 * @param gameState The new board's gamestate.
	 */
	private Board(Board board, GameState gameState) {
		this.currentGameState = gameState;
		this.numberOfRows = board.numberOfRows;
		this.numberOfColumns = board.numberOfColumns;

		this.items = board.items;
	}

	/**
	 * Set an item on a board while preserving purity
	 *
	 * @param item the item you want to add to the board
	 * @return a board which is a result of the applied transformation
	 */
	public Board setItem(BoardItem item) {
		Board modifiedBoard = new Board(this);

		if (item.coordinate.isLeft()) {
			Coordinate coordinate = item.coordinate.left().get();
			modifiedBoard.items = modifiedBoard.items.plus(coordinate, item);
		}

		if (item.coordinate.isRight()) {
			Pair<Coordinate,Coordinate> coordinate =
					item.coordinate.right().get();

			modifiedBoard.items = modifiedBoard.items.plus(coordinate.left(), item);
			modifiedBoard.items = modifiedBoard.items.plus(coordinate.right(), item);
		}

		return modifiedBoard;
	}

	/**
	 * Gets the BoardItem at a specific coordinate.
	 * @param coordinate The coordinate of the board item.
	 * @return The board item.
	 */
	public BoardItem getItem(Coordinate coordinate) {
		return this.items.get(coordinate);
	}

	/**
	 * Returns the persistent map containing the items in the board.
	 * @return The items in the board.
	 */
	public PMap<Coordinate, BoardItem> getItems() {
		return items;
	}

	/**
	 * Returns the map of items in a specific slice along a column.
	 * @param column The column of the slice.
	 * @return The map containing the items in that column.
	 */
	public PMap<Coordinate, BoardItem> getColumnSlice(int column) {
		PMap<Coordinate, BoardItem> slice = HashTreePMap.empty();

		for (int row = 0; row < numberOfRows; row++) {
			Coordinate coordinate = new Coordinate(row, column);
			slice = slice.plus(coordinate, getItem(coordinate));
		}

		return slice;
	}

	/**
	 * Returns the map of items in a specific slice along a row.
	 * @param row The row of the slice.
	 * @return The map containing the items in that row.
	 */
	public PMap<Coordinate, BoardItem> getRowSlice(int row) {
		PMap<Coordinate, BoardItem> slice = HashTreePMap.empty();

		for (int column = 0; column < numberOfColumns; column++) {
			Coordinate coordinate = new Coordinate(row, column);
			slice = slice.plus(coordinate, getItem(coordinate));
		}

		return slice;
	}

	/**
	 * Attempts to slide a fox in a specific direction and returns the resulting board.
	 * @param direction The direction the fox should slide.
	 * @param moveSpaces The amount of spaces it should move.
	 * @param coordinate The coordinate of the fox.
	 * @return The board with the resulting move.
	 * @throws InvalidMoveException If the move is invalid.
	 */
	public Board slide(Direction direction, int moveSpaces,
					   Coordinate coordinate) throws InvalidMoveException {

		Board board = new Board(this);
		BoardItem item = this.items.get(coordinate);

		//if item is a fox, perform the slide
		if (item instanceof Fox) {
			Fox fox = (Fox) item;
			PMap<Coordinate, BoardItem> slice =
					board.getSlice(direction, coordinate);

			Pair<Coordinate, Coordinate> originalCoords =
					Pair.pair(fox.getHead(), fox.getTail());

			EmptyBoardItem emptyHead =
					new EmptyBoardItem(originalCoords.left());

			EmptyBoardItem emptyTail =
					new EmptyBoardItem(originalCoords.right());

			board = board.setItem(emptyHead);
			board = board.setItem(emptyTail);

			Fox newFox = fox.slide(slice,
					moveSpaces, direction);

			board = board.setItem(newFox);
		} else {
			throw new InvalidMoveException("Must be Fox to slide");
		}

		board = board.updateGameState();
		return board;
	}

	/**
	 * Attempts to jump a rabbit at a location on the board, and returns the resulting board.
	 * @param direction The direction the rabbit should jump.
	 * @param coordinate The coordinate of the rabbit.
	 * @return The resulting board after the move.
	 * @throws InvalidMoveException If the move is not valid.
	 */
	public Board jump(Direction direction, Coordinate coordinate)
			throws InvalidMoveException {

		Board board = new Board(this);
		BoardItem item = this.items.get(coordinate);
		Either<Rabbit, ContainerItem> rabbitOrHole;

		PMap<Coordinate, BoardItem> slice = this.getSlice(direction,
				coordinate);

		if (item instanceof Rabbit) {
			Rabbit rabbit = (Rabbit) item;
			rabbitOrHole = rabbit.jump(direction, slice);

			EmptyBoardItem empty = new EmptyBoardItem(coordinate);
			board = board.setItem(empty);

			if (rabbitOrHole.isLeft()) {
				board = board.setItem(rabbitOrHole.left().get());
			} else {
				board = board.setItem(rabbitOrHole.right().get());
			}
		} else if (item instanceof ContainerItem) {
			ContainerItem containerItem = (ContainerItem) item;

			Pair<ContainerItem, Either<Rabbit, ContainerItem>> holeAndJumped
					= containerItem.jump(direction, slice);

			rabbitOrHole = holeAndJumped.right();

			// sets empty hole
			board = board.setItem(holeAndJumped.left());

			// if its a rabbit
			if (rabbitOrHole.isLeft()) {
				Rabbit newRabbit = rabbitOrHole.left().get();
				board = board.setItem(newRabbit);
			}

			// if its a hole
			else {
				ContainerItem newContainerItem = rabbitOrHole.right().get();
				board = board.setItem(newContainerItem);
			}
		} else {
			throw new InvalidMoveException("Must be a rabbit to jump!");
		}

		board = board.updateGameState();
		return board;
	}

	/**
	 * Attempts to move an item at a specific coordinate to another coordinate.
	 * @param itemSelected The coordinate that the item is moved from.
	 * @param itemDestination The coordinate the item is moved to.
	 * @return The resulting board.
	 * @throws InvalidMoveException If the move is invalid.
	 */
	public Board move(Coordinate itemSelected, Coordinate itemDestination)
			throws InvalidMoveException {
		Coordinate deltaCoordinate = this.computeDelta(itemSelected,
				itemDestination);
		Direction direction = this.getDirectionFromDestination(deltaCoordinate);
		BoardItem item = this.getItem(itemSelected);
		Board board = this;

		if (item instanceof Rabbit || item instanceof ContainerItem)  {
			board = this.jump(direction, itemSelected);
			return board;
		}
		if (item instanceof Fox) {
			int moveSpaces = deltaCoordinate.row ;
			if (deltaCoordinate.row == 0) {
				moveSpaces = deltaCoordinate.column;
			}

			board = this.slide(direction, Math.abs(moveSpaces), itemSelected);
			return board;
		}
		throw new InvalidMoveException("Invalid move!");
	}

	/**
	 * Returns the distance coordinate between two different coordinates.
	 * @param initial     The intial coordinate that the movement starts from.
	 * @param destination The final coordinate that it should end up at.
	 * @return
	 */
	private Coordinate computeDelta(Coordinate initial,
									Coordinate destination) {
		int row = destination.row - initial.row;
		int column = destination.column - initial.column;

		return new Coordinate(row, column);
	}

	/**
	 * Returns the direction of a move based on a coordinate containing the
	 * destination's distance.
	 * @param deltaDistance Coordinate of the distance between the start and end point
	 * @return The direction desired
	 * @throws IllegalArgumentException if the direction is invalid
	 */
	private Direction getDirectionFromDestination(Coordinate deltaDistance) {

		if (deltaDistance.row == 0 && deltaDistance.column == 0) {
			throw new IllegalArgumentException("Cannot move to the same " +
					"position");
		} else if (!(deltaDistance.row != 0 && deltaDistance.column != 0)) {
			if (deltaDistance.row > 0) { //destination is below
				return Direction.DOWN;
			} else if (deltaDistance.row < 0) { //destination is above
				return Direction.UP;
			} else if (deltaDistance.column > 0) { //destination is to the right
				return Direction.RIGHT;
			} else { //destination is the the left
				return Direction.LEFT;
			}
		}

		throw new IllegalArgumentException("Invalid direction");
	}

	/**
	 * Updates the gamestate to won if there are no rabbits remaining on the board.
	 */
	public Board updateGameState() {
		for (int row = 0; row < this.numberOfRows; row++) {
			for (int column = 0; column < this.numberOfColumns; column++) {
				// make sure there are no top level rabbits
				if (this.items.get(new Coordinate(row, column)) instanceof Rabbit) {
					GameState currentGameState = GameState.IN_PROGRESS;

					Board board = new Board(this, currentGameState);

					return board;
				}
				// make sure there are no rabbits inside elevated positions
				else if (this.items.get(new Coordinate(row, column)) instanceof ElevatedBoardItem) {
					ElevatedBoardItem elevatedBoardItem = (ElevatedBoardItem)
							this.items.get(new Coordinate(row, column));
					if (elevatedBoardItem.containingItem.isPresent()) {
						Containable containable =
								elevatedBoardItem.containingItem.get();

						if (containable instanceof Rabbit) {
							GameState currentGameState = GameState.IN_PROGRESS;

							Board board = new Board(this, currentGameState);

							return board;
						}
					}
				}
			}
		}

		GameState currentGameState = GameState.SOLVED;

		Board board = new Board(this, currentGameState);

		return board;
	}

	/**
	 * Returns true if the object can move.
	 * @param coordinate of where the object is on the board
	 * @return true if the object is movable.
	 */
	public boolean isMovable(Coordinate coordinate) {
		if (this.getItem(coordinate) instanceof Movable) {
			return true;
		}

		if (this.getItem(coordinate) instanceof ContainerItem) {
			ContainerItem item = (ContainerItem) this.getItem(coordinate);
			if (item.isMovable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string representation of the board.
	 * @return The string representation.
	 */
	@Override
	public String toString() {
		String str = "";
		String rowLine = "";

		for (int i = 0; i < numberOfRows; i++) {
			rowLine += "--------";
		}

		String columnLine = "";

		// column header
		for (int i = 0; i < numberOfRows; i++) {
			columnLine += "     " + (i + 1) + " ";
		}

		columnLine += "\n";

		for (int row = 0; row < numberOfRows; row++) {

			if (row == 0) {
				str += columnLine;
			}

			str += rowLine;
			str += "\n";

			str += "" + (row + 1);
			for (int column = 0; column < numberOfColumns; column++) {
				BoardItem item = getItem(new Coordinate(row, column));

				str += " | ";
				//test code
				if (item.toString().length() == 10) {
					str += " " + item.toString() + " ";
				} else if (item.toString().length() == 11) {
					str += " " + item.toString();
				} else {
					logger.error("badly sized ui text");
				}

				str += " ";
			}

			str += " |\n";
		}

		str += rowLine;

		return str;
	}

	/**
	 * Returns the hashcode of the board.
	 * @return The board's hashcode.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(numberOfRows, numberOfColumns, items, currentGameState);
	}

	/**
	 * Checks if this board is equivalent to another board.
	 * @param o The board being checked against
	 * @return True if the boards are equivalent (their state is the same)
	 */
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (o == null) return false;

		if (this.getClass() != o.getClass())
			return false;

		Board board = (Board) o;

		if ((this.numberOfRows == board.numberOfRows) &&
				(this.numberOfColumns == board.numberOfColumns) && (this.currentGameState == board.currentGameState)){
			return this.hasSameContents(board);
		}
		else {
			return false;
		}
	}


	/**
	 * Checks if this board has the same inner contents as another board(All of the objects inside are in the same places)
	 * @param board The board being checked against
	 * @return
	 */
	private boolean hasSameContents(Board board) {
		for (int i = 0; i < numberOfRows; i++){
			for (int j = 0; j < numberOfColumns; j++){
				try {
					BoardItem thisBoardItem = this.getItem(new Coordinate(i,j));
					BoardItem compareBoardItem = board.getItem(new Coordinate(i,j));
					if (!thisBoardItem.equals(compareBoardItem)){
						return false;
					}
				}
				catch(Exception e){
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Will return the XML representation of the entire board and all the items
	 * contained in the board.
	 * @return the XML representation of the whole board.
	 */
	public String toXML() {
		String xml = "<Board>";

		HashSet<BoardItem> items = new HashSet<>();

		//iterate through the map containing the items on the board.
		for (Coordinate coordinate : this.items.keySet()) {
			//remove duplicates if the item has more than 1 coordinate.
			items.add(this.items.get(coordinate));
		}

		//iterate through the set containing the items on the board removing
		for (BoardItem item : items) {
			//append the string representation of the items on the board.
			xml = xml + item.toXML();
		}

		xml = xml + "</Board>";

		return xml;
	}
}
