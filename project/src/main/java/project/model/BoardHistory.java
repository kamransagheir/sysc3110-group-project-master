package project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manages the history of the board's moves by serializing its states.
 * Has options to undo and redo, recalling old board states that were serialized.
 */
public class BoardHistory {
    /**
     * The list containing the old boards.
     */
    private List<Board> boardHistory;

    /**
     * The current move that the board is on.
     */
    private int currentMove;

    /**
     * Gets the current move of the board.
     * @return The move that is returned.
     */
    public int getCurrentMove() {
        return currentMove;
    }

    /**
     * Sets the current move of the board.
     * @param currentMove The move to be set.
     */
    public void setCurrentMove(int currentMove) {
        this.currentMove = currentMove;
    }

    /**
     * Creates a move history, taking in an initial board to set as its first state.
     */
    public BoardHistory(Board board) {
        boardHistory = new ArrayList<>();
        boardHistory.add(board);
        currentMove = 0;
    }

    /**
     * Adds a board state to the history.
     * @param boardState The board being added to the history.
     */
    public void addState(Board boardState) {
        currentMove++;
        if (!(boardHistory.size() == currentMove)) {
            for (int i = boardHistory.size() - 1; i >= currentMove; i--){
                boardHistory.remove(i);
            }
        }

        boardHistory.add(boardState);
    }

    /**
     * Returns the previous board state in the history, representing an "undo" move.
     * @return The board representing the previous state.
     */
    public Board getUndoBoard(){

        Board lastBoard;
        if (currentMove != 0){
            lastBoard = boardHistory.get(currentMove - 1);
            currentMove--;
        }
        else {
            lastBoard = boardHistory.get(currentMove);
        }
        return lastBoard;
    }

    /**
     * Returns the previous board state that was undone, or the current board if no previous undone boards remain.
     * @return The board representing the redone state.
     */
    public Board getRedoBoard() {
        //Increments the move unless it is already at the end of the move list
        if (!(boardHistory.size() == currentMove + 1)) {
            currentMove++;
        }

        return boardHistory.get(currentMove);
    }
}