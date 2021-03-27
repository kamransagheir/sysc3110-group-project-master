package project.model;

import java.util.List;

import io.atlassian.fugue.Pair;
import org.junit.jupiter.api.Test;
import project.solver.Move;
import project.solver.Solver;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    @Test
    void testRabbitNoMoves() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M R M M
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,3));
        board = board.setItem(new Mushroom(2,4));

        Coordinate rabbitCoordinate = new Coordinate(2,2);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        List<Move> generatedMoves;

        Solver solver = new Solver();
        generatedMoves = solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }

    @Test
    void testRabbitOneMoveRight() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M R M E
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,3));

        Coordinate rabbitCoordinate = new Coordinate(2,2);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Coordinate expectedMove = new Coordinate(2, 4);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 1, "there should only be one move");

        Move move = generatedMoves.get(0);

        assertEquals(rabbit, move.item, "should be trying to move the rabbit");
        assertEquals(Direction.RIGHT, move.direction, "move right");
        assertEquals(rabbitCoordinate, move.initial);
        assertEquals(expectedMove, move.ending);

    }


    @Test
    void testRabbitOneMoveLeft() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    E M R M M
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,3));
        board = board.setItem(new Mushroom(2,4));

        Coordinate rabbitCoordinate = new Coordinate(2,2);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Coordinate expectedMove = new Coordinate(2, 0);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 1, "The list have one move");

        Move move = generatedMoves.get(0);

        assertEquals(rabbit, move.item, "should be trying to move the rabbit");
        assertEquals(Direction.LEFT, move.direction, "move left");
        assertEquals(rabbitCoordinate, move.initial);
        assertEquals(expectedMove, move.ending);

    }


    @Test
    void testRabbitOneMoveLeftOutOfBounds() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    R M M M M
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,2));
        board = board.setItem(new Mushroom(2,3));
        board = board.setItem(new Mushroom(2,4));

        Coordinate rabbitCoordinate = new Coordinate(2,0);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }

    @Test
    void testRabbitOneMoveRightOutOfBounds() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M M M R
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,2));
        board = board.setItem(new Mushroom(2,3));

        Coordinate rabbitCoordinate = new Coordinate(2,4);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }


    @Test
    void testRabbitOneMoveUpOutOfBounds() {
        //   0 1 2 3 4
//      0    E E R E E
//      1    E E M E E
//      2    M M M M M
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,2));
        board = board.setItem(new Mushroom(2,3));
        board = board.setItem(new Mushroom(2,4));

        Coordinate rabbitCoordinate = new Coordinate(0,2);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }

    @Test
    void testRabbitOneMoveDownOutOfBounds() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M M M M
//      3    E E M E E
//      4    E E R E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,2));
        board = board.setItem(new Mushroom(2,3));
        board = board.setItem(new Mushroom(2,4));

        Coordinate rabbitCoordinate = new Coordinate(4,2);
        Rabbit rabbit = new Rabbit(rabbitCoordinate);

        board = board.setItem(rabbit);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesRabbit(board, rabbitCoordinate);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }


    @Test
    void testFoxSlideOneRight() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M F F M
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,4));

        Coordinate foxHead = new Coordinate(2,2);
        Coordinate foxTail = new Coordinate(2,3);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));

        board = board.setItem(fox);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesFox(board, foxHead);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }


    @Test
    void testFoxSlideOneRightOutOfBounds() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M M M F F
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,1));
        board = board.setItem(new Mushroom(2,2));

        Coordinate foxHead = new Coordinate(2,3);
        Coordinate foxTail = new Coordinate(2,4);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));

        board = board.setItem(fox);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMovesFox(board, foxHead);

        assertEquals(generatedMoves.size(), 0, "The list should be empty no legal moves");
    }

    @Test
    void testFoxSlideTwoMoves() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E E
//      2    M F F E E
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));

        Coordinate foxHead = new Coordinate(2,1);
        Coordinate foxTail = new Coordinate(2,2);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));

        board = board.setItem(fox);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMoves(board);

        assertEquals(2,generatedMoves.size(), "the list should have two " +
                "moves");

    }


    @Test
    void testFoxSlideOneMoveAndOneRabbit() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E M
//      2    M F F E R
//      3    E E M E E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(1,4));

        Coordinate foxHead = new Coordinate(2,1);
        Coordinate foxTail = new Coordinate(2,2);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));
        Rabbit rabbit = new Rabbit(new Coordinate(2,4));

        board = board.setItem(fox);
        board = board.setItem(rabbit);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMoves(board);

        assertEquals(2,generatedMoves.size(), "the list should have two " +
                "moves");

    }


    @Test
    void testFoxSlideOneMoveAndTwoRabbit() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E R
//      2    M F F E M
//      3    E E M E R
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,4));

        Coordinate foxHead = new Coordinate(2,1);
        Coordinate foxTail = new Coordinate(2,2);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));
        Rabbit rabbit = new Rabbit(new Coordinate(1,4));
        Rabbit rabbit2 = new Rabbit(new Coordinate(3,4));

        board = board.setItem(fox);
        board = board.setItem(rabbit);
        board = board.setItem(rabbit2);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMoves(board);

        assertEquals(3,generatedMoves.size(), "the list should have two " +
                "moves");

    }



    @Test
    void testJumpOneRabbit() {
        //   0 1 2 3 4
//      0    E E M E E
//      1    E E M E R
//      2    M F E E M
//      3    E F M R E
//      4    E E M E E

        Board board = new Board(5,5);
        board = board.setItem(new Mushroom(0,2));
        board = board.setItem(new Mushroom(1,2));
        board = board.setItem(new Mushroom(3,2));
        board = board.setItem(new Mushroom(4,2));

        board = board.setItem(new Mushroom(2,0));
        board = board.setItem(new Mushroom(2,4));

        Coordinate foxHead = new Coordinate(2,1);
        Coordinate foxTail = new Coordinate(3,1);

        Fox fox = new Fox(Pair.pair(foxHead, foxTail));
        Rabbit rabbit = new Rabbit(new Coordinate(1,4));
        Rabbit rabbit2 = new Rabbit(new Coordinate(3,3));

        board = board.setItem(fox);
        board = board.setItem(rabbit);
        board = board.setItem(rabbit2);

        Solver solver = new Solver();

        List<Move> generatedMoves =
                solver.generateMoves(board);

        assertEquals(5,generatedMoves.size(), "the list should have five " +
                "moves");

    }

    @Test
    void testSolveDefaultBoard() {
        //   0  1  2  3  4
//      0    HM F  U  E  HR
//      1    E  F  E  M  E
//      2    UR E  HE E  U
//      3    E  E  F  F  E
//      4    HE E  UR E  HE

        Board board = new DefaultBoard().getBoard();

        List<Move> moves = Solver.solve(board);


        assertNotNull(moves);

        for (Move move : moves) {
            try {
                board = board.move(move.initial, move.ending);
            } catch (Exception e) {
                fail();
            }
        }

        assertEquals(GameState.SOLVED, board.currentGameState);

    }
}