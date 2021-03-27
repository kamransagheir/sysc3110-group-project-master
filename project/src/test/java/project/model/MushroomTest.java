package project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MushroomTest {

    @Test
    void testMushroomMovable () {
        Mushroom mushroom = new Mushroom(0, 0);
        BoardItem item = mushroom;
        assertFalse(item instanceof Movable);
    }

    @Test
    void testMushroomObstacle () {
        Mushroom mushroom = new Mushroom(0, 0);
        BoardItem item = mushroom;
        assertTrue(item.isObstacle());
    }

    @Test
    void testXML () {
        Mushroom mushroom = new Mushroom(1, 3);
        BoardItem item = mushroom;
        assertEquals("<Mushroom><Coordinate row=" + '"' + 1 + '"' + " column" +
                        "=" + '"' + 3 + '"' +
                        "/></Mushroom>",
                mushroom.toXML(), "The XML representation should be equal");
        assertEquals("<Mushroom><Coordinate row=" + '"' + 1 + '"' + " column" +
                        "=" + '"' + 3 + '"' + "/></Mushroom>",
                item.toXML(), "The XML representation should be equal");
    }
}
