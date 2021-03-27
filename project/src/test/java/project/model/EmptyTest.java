package project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmptyTest {

    @Test
    void testToXML() {
        Coordinate coordinate = new Coordinate(0, 0);
        EmptyBoardItem empty = new EmptyBoardItem(coordinate);

        assertEquals("<Empty><Coordinate row=" + '"' + 0 + '"' + " column=" + '"' + 0 + '"' + "/></Empty>",
                empty.toXML(), "The XML representation should be equal");
    }
}
