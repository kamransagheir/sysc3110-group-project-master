package project.model;

import com.google.common.base.Optional;
import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class RabbitTest {

    @Test
    void constructor () {
        Coordinate coordinate = new Coordinate(0, 0);
        Rabbit rabbit = new Rabbit(coordinate);

        assertNotNull(rabbit, "should not be null");
        assertTrue(rabbit.coordinate.isLeft(), "should be a left" +
                " type");
    }

    @Test
    void testJumpRightOne () {
        // Slice setup
        // Initial
        // R M E
        // Result
        // E M R

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 2);

        Board board = new Board(1,3);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.RIGHT,
                    board.getRowSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpRightTwo () {
        // Slice setup
        // Initial
        // R M M E
        // Result
        // E M M R

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 3);

        Board board = new Board(1,4);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));
        board = board.setItem(new Mushroom(new Coordinate(0 ,2)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.RIGHT,
                    board.getRowSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpRightIntoHole () {
        // Slice setup
        //Initial
        //    R M H
        //Result
        // -> E M H
        // Makes Hole an Obstacle after rabbit is in Hole

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);

        ContainerItem containerItem = new Hole(new Coordinate(0,2), Optional.absent());

        Board board = new Board(1,3);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));
        board = board.setItem(containerItem);

        // Perform jump
        Either<Rabbit, ContainerItem> returnPair =
                null;
        try {
            returnPair = initialRabbit.jump(Direction.RIGHT,
            board.getRowSlice(initialCoordinate.column));
        } catch (InvalidMoveException e) {
            fail();
        }

        ContainerItem newContainerItem = returnPair.right().get();

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // The original hole should not have changed
        assertFalse(containerItem.containingItem.isPresent(), "the hole should still " +
                "be empty");

        // The original rabbit should not have changed
        assertEquals(initialRabbit.coordinate.left().get(), initialCoordinate,
                "the " +
                "original rabbit should not have changed");

        // The new returned hole should have the rabbit
        assertTrue(newContainerItem.containingItem.isPresent(), "the hole should not " +
                "be" +
                " empty");

        assertTrue(newContainerItem.containingItem.get() instanceof Rabbit, "rabbit " +
                "should be in the hole");

    }

    @Test
    void testJumpLeftOne () {
        // Slice setup
        // Initial
        // E M R
        // Result
        // R M E

        Coordinate initialCoordinate = new Coordinate(0, 2);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 0);

        Board board = new Board(1,3);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.LEFT,
                    board.getRowSlice(initialCoordinate.row)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpLeftTwo () {
        // Slice setup
        // Initial
        // E M M R
        // Result
        // R M M E

        Coordinate initialCoordinate = new Coordinate(0, 3);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 0);

        Board board = new Board(1,4);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));
        board = board.setItem(new Mushroom(new Coordinate(0 ,2)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.LEFT,
                    board.getRowSlice(initialCoordinate.row)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpLeftIntoHole () {
        // Slice setup
        //Initial
        //  H M R
        //Result
        //  H(R) M E <-

        Coordinate initialCoordinate = new Coordinate(0, 2);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);

        Hole hole = new Hole(new Coordinate(0,0), Optional.absent());

        Board board = new Board(1,3);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));
        board = board.setItem(hole);

        // Perform jump
        Either<Rabbit, ContainerItem> rabbitOrHole = null;

        try {

            rabbitOrHole = initialRabbit.jump(Direction.LEFT,
                    board.getRowSlice(initialCoordinate.row));

        } catch (InvalidMoveException e) {
            fail();
        }

        Hole newHole = (Hole) rabbitOrHole.right().get();

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // The original hole should not have changed
        assertFalse(hole.containingItem.isPresent(), "the hole should still " +
                "be empty");

        // The original rabbit should not have changed
        assertEquals(initialRabbit.coordinate.left().get(), initialCoordinate,
                "the " +
                        "original rabbit should not have changed");

        // The new returned hole should have the rabbit
        assertTrue(newHole.containingItem.isPresent(), "the hole should not " +
                "be" +
                " empty");
    }

    @Test
    void testJumpDownOne () {
        // Slice setup
        //Initial
        // R
        // M
        // E
        //Result
        // E
        // M
        // R

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(2, 0);

        Board board = new Board(3,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1,0)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.DOWN,
                    board.getColumnSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpDownTwo () {
        // Slice setup
        //Initial
        // R
        // M
        // M
        // E
        //Result
        // E
        // M
        // M
        // R

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(3, 0);

        Board board = new Board(4,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1 ,0)));
        board = board.setItem(new Mushroom(new Coordinate(2 ,0)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.DOWN,
                    board.getColumnSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpDownIntoHole () {
        // Slice setup
        //Initial
        //  R
        //  M
        //  H
        //Result
        //  E
        //  M
        //  HR

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);

        Hole hole = new Hole(new Coordinate(2,0), Optional.absent());

        Board board = new Board(3,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1 ,0)));
        board = board.setItem(hole);

        // Perform jump
        Either<Rabbit, ContainerItem> returnPair =
                null;
        try {
            returnPair = initialRabbit.jump(Direction.DOWN,
                    board.getColumnSlice(initialCoordinate.column));
        } catch (InvalidMoveException e) {
            fail();
        }

        Hole newHole = (Hole) returnPair.right().get();

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // The original hole should not have changed
        assertFalse(hole.containingItem.isPresent(), "the hole should still " +
                "be empty");

        // The original rabbit should not have changed
        assertEquals(initialRabbit.coordinate.left().get(), initialCoordinate,
                "the " +
                        "original rabbit should not have changed");

        // The new returned hole should have the rabbit
        assertTrue(newHole.containingItem.isPresent(), "the hole should not " +
                "be" +
                " empty");
    }

    @Test
    void testJumpUpOne () {
        // Slice setup
        //Initial
        // E
        // M
        // R
        //Result
        // R
        // M
        // E

        Coordinate initialCoordinate = new Coordinate(2, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 0);

        Board board = new Board(3,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1,0)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.UP,
                    board.getColumnSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");

    }

    @Test
    void testJumpUpTwo () {
        // Slice setup
        //Initial
        // E
        // M
        // M
        // R
        //Result
        // R
        // M
        // M
        // E

        Coordinate initialCoordinate = new Coordinate(3, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        Coordinate expectedJumpCoordinate = new Coordinate(0, 0);

        Board board = new Board(4,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1 ,0)));
        board = board.setItem(new Mushroom(new Coordinate(2 ,0)));

        // Perform jump
        Rabbit newRabbit = null;
        try {
            newRabbit = initialRabbit.jump(Direction.UP,
                    board.getColumnSlice(initialCoordinate.column)).left().get();
        } catch (InvalidMoveException e) {
            fail();
        }

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // Check the new rabbit coordinates
        assertNotNull(initialRabbit);
        assertNotEquals(initialRabbit, newRabbit, "the new rabbit should be " +
                "different to the old coordinate");
        Coordinate coordinate = newRabbit.coordinate.left().get();
        assertEquals(expectedJumpCoordinate, coordinate, "we should have " +
                "jumped to our expected coordinates");
    }

    @Test
    void testJumpUpIntoHole () {
        // Slice setup
        //Initial
        //  H
        //  M
        //  R
        //Result
        //  H
        //  M
        //  E

        Coordinate initialCoordinate = new Coordinate(2, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);

        Hole hole = new Hole(new Coordinate(0,0), Optional.absent());

        Board board = new Board(3,1);
        board = board.setItem(initialRabbit);
        board = board.setItem(new Mushroom(new Coordinate(1 ,0)));
        board = board.setItem(hole);

        // Perform jump
        Either<Rabbit, ContainerItem> returnPair =
                null;
        try {
            returnPair = initialRabbit.jump(Direction.UP,
                    board.getColumnSlice(initialCoordinate.column));
        } catch (InvalidMoveException e) {
            fail();
        }

        Hole newHole = (Hole) returnPair.right().get();

        // Make sure the initial rabbit has not been mutated
        Coordinate initialRabbitCoordinate = initialRabbit.coordinate.left().get();
        assertEquals(initialCoordinate, initialRabbitCoordinate, "the initial" +
                " rabbit should not have been mutated" );

        // The original hole should not have changed
        assertFalse(hole.containingItem.isPresent(), "the hole should still " +
                "be empty");

        // The original rabbit should not have changed
        assertEquals(initialRabbit.coordinate.left().get(), initialCoordinate,
                "the " +
                        "original rabbit should not have changed");


        // The new returned hole should have the rabbit
        assertTrue(newHole.containingItem.isPresent(), "the hole should not " +
                "be" +
                " empty");
    }

    @Test
    void testXML () {
        Rabbit rabbit = new Rabbit(1, 3);
        BoardItem item = rabbit;
        assertEquals("<Rabbit><Coordinate row=" + '"' + 1 + '"' + " column=" + '"' + 3 + '"' +
                        "/></Rabbit>",
                rabbit.toXML(), "The XML representation should be equal");
        assertEquals("<Rabbit><Coordinate row=" + '"' + 1 + '"' + " column=" + '"' + 3 + '"' +
                        "/></Rabbit>",
                item.toXML(), "The XML representation should be equal");
    }
}
