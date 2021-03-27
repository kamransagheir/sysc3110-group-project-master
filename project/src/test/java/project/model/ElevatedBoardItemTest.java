package project.model;

import org.junit.jupiter.api.Test;
import com.google.common.base.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatedBoardItemTest {

    @Test
    void toXMLWithoutContainingItem() {
        Coordinate coordinate = new Coordinate(0, 0);

        ElevatedBoardItem elevatedBoardItem =
                new ElevatedBoardItem(coordinate, Optional.absent());

        assertEquals("<ElevatedBoardItem><Coordinate row=" + '"' + 0 + '"' +
                " column=" + '"' + 0 + '"' + "/></ElevatedBoardItem>",
                elevatedBoardItem.toXML(),
                "The xml representations should be the same.");

    }

    @Test
    void toXMLWithContainingItem() {
        Coordinate coordinate = new Coordinate(0, 0);
        Rabbit rabbit = new Rabbit(coordinate);

        ElevatedBoardItem elevatedBoardItem =
                new ElevatedBoardItem(coordinate, Optional.of(rabbit));

        assertEquals("<ElevatedBoardItem><Coordinate row=" + '"' + 0 + '"' +
                        " column=" + '"' + 0 + '"' + "/><Rabbit><Coordinate " +
                        "row=" + '"' + 0 + '"' + " column=" + '"' + 0 + '"' + "/>" +
                        "</Rabbit></ElevatedBoardItem>",
                elevatedBoardItem.toXML(),
                "The xml representations should be the same.");

    }
}
