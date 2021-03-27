package project.model;

import com.google.common.base.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerItemTest {

    @Test
    void testConstructor() {
        Optional<Containable> containable = Optional.absent();
        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                containable);

        assertEquals(containerItem.containingItem, containable,
                "the hole should have the item");

        Coordinate instanceCoordinate = containerItem.coordinate.left().get();

        assertEquals(holeCoordinate, instanceCoordinate,
                "the hole should have the correct coordinate");
    }


    @Test
    void testConstructorWithItem() {
        Containable containableItem = new Rabbit(new Coordinate(0,0));
        Optional<Containable> containable = Optional.of(containableItem);

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                containable);

        assertEquals(containerItem.containingItem, containable,
                "the hole should have the item");

        Containable item = containerItem.containingItem.get();
        BoardItem boardItem = (BoardItem) item;

        assertEquals(holeCoordinate, boardItem.coordinate.left().get(),
                "the containableItem should have the correct coordinate");
    }


    @Test
    void testIsObstacleWithoutItem() {

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                Optional.absent());

        assertFalse(containerItem.isObstacle(),
                "the hole is not an obstacle when empty");
    }

    @Test
    void testIsObstacleWithItem() {
        Rabbit rabbit = new Rabbit(new Coordinate(0,0));
        Optional<Containable>  containableRabbit = Optional.of(rabbit);

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                containableRabbit);

        assertTrue(containerItem.isObstacle(),
                "the hole is an obstacle when containing a rabbit");
    }

    @Test
    void testIsMovableWithoutItem() {

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                Optional.absent());

        assertFalse(containerItem.isMovable(),
                "the hole is not a movable when empty");
    }

    @Test
    void testIsMovableWithItemThatIsMovable() {
        Rabbit rabbit = new Rabbit(new Coordinate(0,0));
        Optional<Containable>  containableRabbit = Optional.of(rabbit);

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                containableRabbit);

        assertTrue(containerItem.isMovable(),
                "the hole is a movable when containing a rabbit");
    }

    @Test
    void testIsMovableWithItemThatIsNotMovable() {
        Mushroom mushroom = new Mushroom(new Coordinate(0,0));
        Optional<Containable>  containableMushroom = Optional.of(mushroom);

        Coordinate holeCoordinate = new Coordinate(0, 0);
        ContainerItem containerItem = new Hole(holeCoordinate,
                containableMushroom);

        assertFalse(containerItem.isMovable(),
                "the hole is not a movable when containing a mushroom");
    }

    @Test
    void testJumpRabbitOutOfHole(){
        // Slice setup
        // H(R) M E
        // Final:
        // H M R

        Coordinate initialCoordinate = new Coordinate(0, 0);
        Rabbit initialRabbit = new Rabbit(initialCoordinate);
        ContainerItem initialContainerItem = new Hole(initialCoordinate, Optional.of(initialRabbit));
        Coordinate expectedJumpCoordinate = new Coordinate(0, 2);

        Board board = new Board(1,3);
        board = board.setItem(initialContainerItem);
        board = board.setItem(new Mushroom(new Coordinate(0 ,1)));

        // Perform jump
        try {
            board = board.jump(Direction.RIGHT, initialCoordinate);
        } catch (InvalidMoveException e) {
            fail(e);
        }

        Rabbit newRabbit = (Rabbit) board.getItem(expectedJumpCoordinate);
        ContainerItem newContainerItem = (ContainerItem) board.getItem(initialCoordinate);
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

        //Make sure the new hole is not the same as the old one
        assertNotEquals(newContainerItem, initialContainerItem);

        //Ensure the new hole is empty
        assertEquals(Optional.absent(), newContainerItem.containingItem);
    }
}
