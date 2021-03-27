package project.solver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcollections.PVector;
import org.pcollections.TreePVector;
import project.model.Direction;
import project.model.GameState;
import project.model.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Solver class contains the logic for an algorithm that solves the board
 * from any position.
 */
public class Solver {

    /**
     * Logger used to log helpful debug statements and errors.
     */
    public static Logger logger = LogManager.getLogger(Solver.class);

    /**
     *Max depth of the "tree" branches used in the algorithm.
     * Number of generations from the root node that the "tree" can contain.
     */
    final static int MAX_DEPTH = 10;

    /**
     * Helpful debugging tool used to log the moves given.
     * @param moves a list that contains valid moves
     */
    private static void printMoves(List<Move> moves) {
        for (Move move: moves) {
            logger.debug(move);
        }
    }

    /**
     * Move a piece on the board.
     * @param board to be solved by moving all the rabbits in the holes.
     * @param move to be applied to the board and move an animal.
     * @return board.move calls the move method of the board to perform the a
     * board with the applied move is then returned.
     * move given as a parameter.
     * @throws InvalidMoveException if the move is invalid.
     */
    private static Board applyMove(Board board, Move move) throws
            InvalidMoveException {
        try {
            return board.move(move.initial, move.ending);
        } catch (Exception e) {
            logger.error("received an error when applying a move");
            throw e;
        }
    }


    /**
     * Solve the board using the tree algorithm.
     * @param board to be solved.
     * @return reversed, a list of moves to be executed that solve board from
     * the root node moving down.
     */
    public static List<Move> solve (Board board) {
        PVector<Board> boardHistory = TreePVector.empty();

        List<Move> solutions = solve(board, boardHistory, 0);

        List<Move> reversed =  new ArrayList<>();

        logger.debug("generated moves");
        printMoves(reversed);


        for (int i = solutions.size() - 1; i >= 0; i--) {
            reversed.add(solutions.get(i));
        }

        return reversed;
    }

    /**
     * applies algorithm to solve the board using a brute force tree solution.
     * @param board to be solved.
     * @param boardHistory previous board states when trying to solve the
     *                     board to avoid duplicated moves.
     * @param depth of the branch so far to avoid going past the max depth.
     * @return list moves to be executed to solve the board in reverse order
     * (starting from leaf node).
     */
    private static PVector<Move> solve (Board board,
                           PVector<Board> boardHistory,
                           int depth) {

        PVector<Board> history = boardHistory;

        if (depth > MAX_DEPTH) {
            return null;
        }

        if (board.currentGameState == GameState.IN_PROGRESS) {
            // board is the root of the tree

            List<Move> moves = generateMoves(board);

            for (Move move: moves) {
                try {
                    Board newBoard = applyMove(board, move);

                   if (!history.contains(newBoard)) {

                        history = history.plus(newBoard);

                       PVector<Move> solution = solve(newBoard, history,
                               depth + 1);

                        if(solution != null) {
                            solution = solution.plus(move);
                            return solution;
                        }
                   }

                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }

        else {
            logger.debug("******************Game solved****************");
            logger.debug(board);

            PVector<Move> solvedMoves = TreePVector.empty();

            return solvedMoves;
        }

        return null;
    }

    /**
     * Generate the legal moves for items on a particular board.
     * @param board to have the legal moves generated on.
     * @return legalMoves, a list of the legal moves generated.
     */
    public static List<Move> generateMoves(Board board) {
        logger.trace("generate moves");
        List<Move> legalMoves = new ArrayList<>();


        for (int row = 0; row < board.numberOfRows; row++) {
            for (int column = 0; column < board.numberOfRows; column++) {

                Coordinate coordinate = new Coordinate(row, column);
                BoardItem item = board.getItem(coordinate);

                if (item instanceof Rabbit) {
                    List<Move> rabbitMoves = generateMovesRabbit(board,
                            coordinate);
                    logger.trace("rabbit moves generated: " + rabbitMoves.size());

                    legalMoves.addAll(rabbitMoves);
                }

                if (item instanceof ContainerItem) {
                    if (((ContainerItem) item).containingItem.isPresent()) {
                        ContainerItem containerItem = (ContainerItem) item;

                        Containable containable =
                                containerItem.containingItem.get();

                        if (containable instanceof Rabbit)  {
                            List<Move> rabbitMoves = generateMovesRabbit(board,
                                    coordinate);
                            logger.trace("rabbit moves generated: " + rabbitMoves.size());

                            legalMoves.addAll(rabbitMoves);
                        }
                    }
                }

                else if (item instanceof Fox) {
                    List<Move> foxMoves = generateMovesFox(board, coordinate);
                    logger.trace("fox moves generated: " + foxMoves.size());
                    legalMoves.addAll(foxMoves);
                }
            }
        }

        return legalMoves;
    }

    /**
     * Generate the legal moves for the fox piece
     * @param board to be solved.
     * @param coordinate the coordinate the fox to be moved is initially at.
     * @return legalMoves, list of legal moves generated for the fox item.
     */
    public static List<Move> generateMovesFox(Board board,
                                            Coordinate coordinate) {
        logger.trace("generate moves");
        List<Move> legalMoves = new ArrayList<>();
        Fox item = (Fox) board.getItem(coordinate);
        Coordinate destination = new Coordinate(coordinate.row, coordinate.column);

        if (item.orientation  == Orientation.HORIZONTAL) {
            // .equals??
            int nextColumn;

            //check slide left
            for (nextColumn = coordinate.column - 1; nextColumn >= 0; nextColumn--) {
                destination = new Coordinate(coordinate.row, nextColumn);

                // If we don't hit an obstacle
                if (!board.getItem(destination).isObstacle()) {
                    Move move = new Move(item, Direction.LEFT, coordinate, destination);
                    legalMoves.add(move);
                }
                else {
                    break;
                }
            }

            //check slide right
            for (nextColumn = coordinate.column + 1; nextColumn < board.numberOfColumns; nextColumn++) {
                destination = new Coordinate(coordinate.row, nextColumn);

                // If we don't hit an obstacle
                if (!board.getItem(destination).isObstacle()) {
                    Move move = new Move(item, Direction.RIGHT, coordinate, destination);
                    legalMoves.add(move);
                } else {
                    break;
                }
            }

        } else if(item.orientation == Orientation.VERTICAL) {
            int nextRow;
            //check slide up
            for (nextRow = coordinate.row - 1; nextRow >= 0; nextRow--) {
                destination = new Coordinate(nextRow, coordinate.column);

                if (!board.getItem(destination).isObstacle()) {
                    Move move = new Move(item, Direction.UP, coordinate, destination);
                    legalMoves.add(move);
                }
                else {
                    break;
                }
            }

            //check slide down
            for (nextRow = coordinate.row + 1; nextRow < board.numberOfRows; nextRow++) {
                destination = new Coordinate(nextRow, coordinate.column);

                if (!board.getItem(destination).isObstacle()) { //can't slide
                    // into obstacle
                    Move move = new Move(item, Direction.DOWN, coordinate, destination);
                    legalMoves.add(move);
                }
                else {
                    break;
                }
            }
        }

        return legalMoves;
    }

    /**
     * Verify that coordinate is in bounds of the board.
     * @param coordinate to be checked.
     * @param board used to calculate legal bounds.
     * @return true or false based on if the coordinate is in bounds or not.
     */
    private static boolean isCoordinateInBoard(Coordinate coordinate,
                                            Board board) {
        // Subtract 1 for zero indexing
        int maxRows = board.numberOfRows - 1;
        int maxColumns = board.numberOfColumns - 1;

        int minRows = 0;
        int minColumns = 0;

        if (coordinate.row < minRows || coordinate.row > maxRows) {
            return false;
        }

        if (coordinate.column < minColumns || coordinate.column > maxColumns) {
            return false;
        }

        return true;
    }

    /**
     * Generate legal moves for a rabbit object on the board.
     * @param board to be solved.
     * @param coordinate the rabbit is initially at.
     * @return legalMoves, list of legal moves generated for the rabbit item.
     */
    public static List<Move> generateMovesRabbit(Board board,
                                            Coordinate coordinate) {
        logger.trace("generate moves");
        List<Move> legalMoves = new ArrayList<>();
        BoardItem item = board.getItem(coordinate);
        Coordinate nextItem;

        //check up jump
        nextItem = new Coordinate(coordinate.row - 1, coordinate.column);

        if (isCoordinateInBoard(nextItem, board)) {
            if (board.getItem(nextItem).isObstacle()) { //rabbits must jump over obstacle to move
                for (int row = coordinate.row - 2; row >= 0; row--) {
                    nextItem = new Coordinate(row, nextItem.column);

                    if (!isCoordinateInBoard(nextItem, board)) {
                        break;
                    }

                    if (!board.getItem(nextItem).isObstacle()) {
                        Move move = new Move(item, Direction.UP, coordinate, nextItem);
                        legalMoves.add(move);
                        break;
                    }
                }
            }
        }

        //check down jump
        nextItem = new Coordinate(coordinate.row + 1, coordinate.column);

        if (isCoordinateInBoard(nextItem, board)) {
            if (board.getItem(nextItem).isObstacle()) { //rabbits must jump over obstacle to move
                for (int row = coordinate.row + 2; row < board.numberOfRows; row++) {
                    nextItem = new Coordinate(row, nextItem.column);

                    if (!isCoordinateInBoard(nextItem, board)) {
                        break;
                    }

                    if (!board.getItem(nextItem).isObstacle()) {
                        Move move = new Move(item, Direction.DOWN, coordinate, nextItem);
                        legalMoves.add(move);
                        break;
                    }
                }
            }
        }

        //check left jump
        nextItem = new Coordinate(coordinate.row, coordinate.column - 1);

        if (isCoordinateInBoard(nextItem, board)) {
            if (board.getItem(nextItem).isObstacle()) {
                //rabbits must jump over obstacle to move
                for (int column = coordinate.column - 2; column >= 0; column--) {
                    nextItem = new Coordinate(nextItem.row, column);

                    if (!isCoordinateInBoard(nextItem, board)) {
                        break;
                    }

                    if (!board.getItem(nextItem).isObstacle()) {
                        Move move = new Move(item, Direction.LEFT, coordinate, nextItem);
                        legalMoves.add(move);
                        break;
                    }
                }
            }
        }

        //check right jump
        nextItem = new Coordinate(coordinate.row, coordinate.column + 1);

        if (isCoordinateInBoard(nextItem, board)) {
            if (board.getItem(nextItem).isObstacle()) {
                //rabbits must jump over obstacle to move
                for (int column = coordinate.column + 2; column < board.numberOfColumns; column++) {
                    nextItem = new Coordinate(nextItem.row, column);

                    if (!isCoordinateInBoard(nextItem, board)) {
                        break;
                    }

                    if (!board.getItem(nextItem).isObstacle()) {
                        Move move = new Move(item, Direction.RIGHT, coordinate,
                                nextItem);
                        legalMoves.add(move);
                        break;
                    }
                }
            }
        }

        return legalMoves;
    }

}
