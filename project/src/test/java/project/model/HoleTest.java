package project.model;

import org.junit.jupiter.api.Test;
import com.google.common.base.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class HoleTest {

    @Test
    void toXMLWithoutContainingItem() {
        Coordinate coordinate = new Coordinate(0, 0);

        Hole hole = new Hole(coordinate, Optional.absent());

        assertEquals("<Hole><Coordinate row=" + '"' + 0 + '"' + " column="
                        + '"' + 0 + '"' + "/></Hole>", hole.toXML(),
                "The xml representations should be the same.");

    }

    @Test
    void toXMLWithContainingItem() {
        Coordinate coordinate = new Coordinate(0, 0);
        Rabbit rabbit = new Rabbit(coordinate);

        Hole hole = new Hole(coordinate, Optional.of(rabbit));

        assertEquals("<Hole><Coordinate row=" + '"' + 0 + '"' + " column="
                        + '"' + 0 + '"' + "/><Rabbit><Coordinate row=" + '"' + 0 + '"' +
                        " column=" + '"' + 0 + '"' + "/></Rabbit></Hole>",
                hole.toXML(),
                "The xml representations should be the same.");

    }
}
