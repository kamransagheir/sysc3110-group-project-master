package project.view;

import com.google.common.base.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.ItemTypes;
import project.model.*;
import project.model.Hole;
import project.model.Mushroom;
import project.model.Rabbit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ItemSelection {

    public static Logger logger = LogManager.getLogger(ItemSelection.class);

    public static Optional<BoardItem> show(JFrame frame,
                                           Coordinate coordinate) {

        logger.debug("user selected coordinate: " + coordinate);

        List<String> possibilities = new ArrayList<>();

        for (ItemTypes t: ItemTypes.values()) {
            possibilities.add(t.getType());
        }

        String result = (String) JOptionPane.showInputDialog(frame, "What board piece " +
                        "would you like " +
                "to place here?. \nNote: if placing a fox select the head " +
                        "position",
                "Item selector",
                JOptionPane.PLAIN_MESSAGE,
                null, possibilities.toArray(), "Empty Item");

        if (result == null) {
            logger.debug("user selected null");
        }
        else {
            logger.debug("user selected: \"" + result + "\'");

            // TODO: rename to ItemType singular?
            ItemTypes selectedType = ItemTypes.fromString(result);

            switch (selectedType) {
                case EmptyItem:
                    return Optional.of(new EmptyBoardItem(coordinate));
                case Rabbit:
                    return Optional.of(new Rabbit(coordinate));
                case Mushroom:
                    return Optional.of(new Mushroom(coordinate));
                case EmptyHole:
                    return Optional.of(new Hole(coordinate));
                case EmptyElevated:
                    return Optional.of(new ElevatedBoardItem(coordinate));
                case HoleRabbit:
                    Rabbit rabbit = new Rabbit(coordinate);
                    Optional<Containable> rabbitOptional = Optional.of (rabbit);

                    return Optional.of(new Hole(coordinate, rabbitOptional));
                case ElevatedRabbit:
                    // Renamed variables due to Java's switch statement
                    // scoping rules
                    Rabbit rabbitE = new Rabbit(coordinate);
                    Optional<Containable> rabbitOptionalE =
                            Optional.of (rabbitE);

                    return Optional.of(new ElevatedBoardItem(coordinate,
                            rabbitOptionalE));
                case HoleMushroom:
                    Mushroom mushroom = new Mushroom(coordinate);
                    Optional<Containable> mushroomOptional =
                            Optional.of (mushroom);

                    return Optional.of(new Hole(coordinate,
                            mushroomOptional));
                case ElevatedMushroom:
                    // Renamed variables due to Java's switch statement
                    // scoping rules
                    Mushroom mushroomE = new Mushroom(coordinate);
                    Optional<Containable> mushroomOptionalE =
                            Optional.of (mushroomE);

                    return Optional.of(new ElevatedBoardItem(coordinate, mushroomOptionalE));
                case Fox:
                    // Using the absent to signal a fox creation
                    return Optional.absent();
                default:
                    throw new IllegalArgumentException("Unknown type");
            }
        }
        throw new IllegalArgumentException("Unknown type");
    }

}
