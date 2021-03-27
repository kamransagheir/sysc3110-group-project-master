package project.model;

import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class FoxTest {

    private static Logger logger = LogManager.getLogger(FoxTest.class);

    @Test
    void testConstructor() {
        Coordinate head = new Coordinate(5,5);
        Coordinate tail = new Coordinate(6, 5);
        Pair<Coordinate, Coordinate> coords = Pair.pair(head, tail);

        Fox fox = new Fox(coords);

        Coordinate expectedHead = new Coordinate(fox.getHead());
        Coordinate expectedTail = new Coordinate(fox.getTail());

        assertEquals(head, expectedHead, "Head coordinates should be same");
        assertEquals(tail, expectedTail, "Tail coordinates should be same");
        assertEquals(Orientation.VERTICAL, fox.orientation, "Orientation should be same");
    }

    @Test
    void slideIntoHole() {
        //slice setup
        //F F H E
        Coordinate destination = new Coordinate(0, 2);
        Fox slidingFox = new Fox(Pair.pair(new Coordinate(0,0),
                new Coordinate(0, 1)));
        Board board = new Board(1, 4);
        board = board.setItem(slidingFox);
        board = board.setItem(new Hole(destination, Optional.absent()));

        Board finalBoard = board;
        assertThrows(Exception.class, () -> {
            slidingFox.slide(finalBoard.getRowSlice(0), 1 , Direction.RIGHT);
        });
    }

    @Test
    void slideRight() {
        //slice setup
        // F F E E
        // E E F F
        Coordinate originalHead = new Coordinate(0,0);
        Coordinate originalTail = new Coordinate(0, 1);
        Fox slidingFox = new Fox(Pair.pair(originalHead, originalTail));
        Board board = new Board(1, 4);
        board = board.setItem(slidingFox);

        Coordinate expectedHead = new Coordinate(0, 2);
        Coordinate expectedTail = new Coordinate(0, 3);

        int moveSpaces = 2;
        try {
            Fox newFox = slidingFox.slide(board.getRowSlice(0), moveSpaces,
                    Direction.RIGHT);
            assertEquals(expectedTail, newFox.getTail(), "the tail should be at " +
                    "the expected coordinate");
            assertEquals(expectedHead, newFox.getHead(),
                    "The head should have moved here");
        } catch (Exception e) {
            logger.debug(e);
            fail();
        }

        // Test that the old fox head did not change
        assertEquals(originalHead, slidingFox.getHead());

        //Test that the old fox tail did not change
        assertEquals(originalTail, slidingFox.getTail());
    }

    @Test
    void slideLeft() {
        //slice setup
        // E E F F
        // F F E E
        Coordinate originalHead = new Coordinate(0,2);
        Coordinate originalTail = new Coordinate(0, 3);
        Fox slidingFox = new Fox(Pair.pair(originalHead, originalTail));
        Board board = new Board(1, 4);
        board = board.setItem(slidingFox);

        Coordinate expectedHead = new Coordinate(0, 0);
        Coordinate expectedTail = new Coordinate(0, 1);

        int moveSpaces = 2;
        try {
            Fox newFox = slidingFox.slide(board.getRowSlice(0), moveSpaces,
                    Direction.LEFT);
            assertEquals(expectedTail, newFox.getTail(), "the tail should be at " +
                    "the expected coordinate");
            assertEquals(expectedHead, newFox.getHead(),
                    "The head should have moved here");
        } catch (Exception e) {
            logger.debug(e);
            fail();
        }

        // Test that the old fox head did not change
        assertEquals(originalHead, slidingFox.getHead());

        //Test that the old fox tail did not change
        assertEquals(originalTail, slidingFox.getTail());
    }

    @Test
    void slideDown() {
        //slice setup
        // F
        // F
        // E
        // E

        // Expected
        // E
        // E
        // F
        // F
        Coordinate originalHead = new Coordinate(0,0);
        Coordinate originalTail = new Coordinate(1, 0);
        Fox slidingFox = new Fox(Pair.pair(originalHead, originalTail));
        Board board = new Board(4, 1);
        board = board.setItem(slidingFox);

        Coordinate expectedHead = new Coordinate(2, 0);
        Coordinate expectedTail = new Coordinate(3, 0);

        int moveSpaces = 2;
        try {
            Fox newFox = slidingFox.slide(board.getColumnSlice(0), moveSpaces,
                    Direction.DOWN);
            assertEquals(expectedTail, newFox.getTail(), "the tail " +
                    "should be at the expected coordinate");
            assertEquals(expectedHead, newFox.getHead(),
                    "The head should have moved here");
        } catch (Exception e) {
            logger.debug(e);
            fail();
        }

        // Test that the old fox head did not change
        assertEquals(originalHead, slidingFox.getHead());

        //Test that the old fox tail did not change
        assertEquals(originalTail, slidingFox.getTail());
    }

    @Test
    void slideUp() {
        //slice setup
        // E
        // E
        // F
        // F

        // Expected
        // F
        // F
        // E
        // E
        Coordinate originalHead = new Coordinate(2,0);
        Coordinate originalTail = new Coordinate(3, 0);
        Fox slidingFox = new Fox(Pair.pair(originalHead, originalTail));
        Board board = new Board(4, 1);
        board = board.setItem(slidingFox);

        Coordinate expectedHead = new Coordinate(0, 0);
        Coordinate expectedTail = new Coordinate(1, 0);

        int moveSpaces = 2;
        try {
            Fox newFox = slidingFox.slide(board.getColumnSlice(0), moveSpaces,
                    Direction.UP);
            assertEquals(expectedTail, newFox.getTail(), "the tail " +
                    "should be at the expected coordinate");
            assertEquals(expectedHead, newFox.getHead(),
                    "The head should have moved here");
        } catch (Exception e) {
            logger.debug(e);
            fail();
        }

        // Test that the old fox head did not change
        assertEquals(originalHead, slidingFox.getHead());

        //Test that the old fox tail did not change
        assertEquals(originalTail, slidingFox.getTail());
    }

    @Test
    void slideRightOffBoard() {
        //slice setup
        // F F E E
        // E E F F
        Coordinate originalHead = new Coordinate(0,0);
        Coordinate originalTail = new Coordinate(0, 1);
        Fox slidingFox = new Fox(Pair.pair(originalHead, originalTail));
        Board board = new Board(1, 4);
        board = board.setItem(slidingFox);

        Coordinate expectedHead = new Coordinate(0, 2);
        Coordinate expectedTail = new Coordinate(0, 3);

        int moveSpaces = 2;
        try {
            Fox newFox = slidingFox.slide(board.getRowSlice(0), moveSpaces,
                    Direction.RIGHT);
            assertEquals(expectedTail, newFox.getTail(), "the tail should be at " +
                    "the expected coordinate");
            assertEquals(expectedHead, newFox.getHead(),
                    "The head should have moved here");
        } catch (Exception e) {
            logger.debug(e);
            fail();
        }

        // Test that the old fox head did not change
        assertEquals(originalHead, slidingFox.getHead());

        //Test that the old fox tail did not change
        assertEquals(originalTail, slidingFox.getTail());
    }

    @Test
    void testXML () {
        Coordinate head = new Coordinate(0, 0);
        Coordinate tail = new Coordinate(0, 1);

        Pair<Coordinate, Coordinate> coordinates = Pair.pair(head, tail);

        Fox fox = new Fox(coordinates);
        BoardItem item = fox;

        assertEquals("<Fox><CoordinatePair headRow=" + '"' + 0 + '"' + " " +
                        "headColumn=" + '"' + 0 + '"' +
                        " tailRow=" + '"' + 0 + '"' + " tailColumn=" + '"' + 1 + '"' + "/></Fox>",
                fox.toXML(), "The XML representation should be equal");
        assertEquals("<Fox><CoordinatePair headRow=" + '"' + 0 + '"' +
                        " headColumn=" + '"' + 0 + '"' + " tailRow=" + '"' + 0 + '"' +
                        " tailColumn=" + '"' + 1 + '"' + "/></Fox>",
                item.toXML(), "The XML representation should be equal");
    }
}