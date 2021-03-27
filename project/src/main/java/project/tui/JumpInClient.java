package project.tui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.Coordinate;
import project.model.Direction;
import project.model.Board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: JumpInClient
 * Description: This class is used for string input parsing
 * and parses them into commands that can be applied to the board
 */
public class JumpInClient {

    private static Logger logger = LogManager.getLogger(JumpInClient.class);
    private static Pattern regexPatternFox = Pattern.compile("Slide\\s+Fox\\s+(\\d)," +
            "\\s*(\\d)\\s+(\\d)\\s+" +
            "(up|down|left|right)", Pattern.CASE_INSENSITIVE);

    private static Pattern regexPatternRabbit = Pattern.compile("Jump\\s+Rabbit\\s+(\\d)," +
            "\\s*(\\d)\\s+" +
            "(up|down|left|right)", Pattern.CASE_INSENSITIVE);

    /**
     * Display standard example inputs message
     * @param board is the object whose state is displayed at the beginning of the prompt
     * @return String containing message with instructions to be displayed to the user
     */
    public String getPrompt(Board board) {
        String prompt = "";

        prompt += board.toString();

        prompt += "\n";

        prompt += "Please type one of the following commands: " +
                "\n";
        prompt += ANSIColor.GREEN + "-> Jump Rabbit row (current row" +
                " of Rabbit), " + "column " +
                "(current column of Rabbit) Direction(Right, Left, Up, Down)"
                + ANSIColor.RESET
                + "\n" ;

        prompt += ANSIColor.GREEN + "-> Slide Fox row (e.g., 1), columns" +
                " (e.g.," + " 2) Number of board " +
                "units / spaces (e.g., 2)" + ANSIColor.RESET +
                "\n";

        prompt += ANSIColor.YELLOW + "Sample commands: \n Jump Rabbit 1,1 " +
                "Right" +
                " \n " +
                "Slide" +
                " Fox " +
                "1,2 2 Left" + ANSIColor.RESET
                + "\n" ;

        prompt += ANSIColor.CYAN + "Please enter command: " + ANSIColor.RESET;
        prompt += "\n";

        return prompt;
    }

    /**
     * Parses Rabbit input
     * @param line to be parsed and used to attempt a move on a rabbit
     * @return command with coordinates used to move the rabbit on the board
     */
    public RabbitCommand parseRabbitCommand(String line) throws Exception {
        // Jump Rabbit 1,2 left
        // Capture groups
        // 1: Coordinate row : 1
        // 2: Coordinate column : 2
        // 3: Direction : left

        Matcher matcher = regexPatternRabbit.matcher(line);

        logger.trace("attempting to parse rabbit command");

        if (matcher.find()) {

            // Extract arguments
            Coordinate coordinate =
                    new Coordinate(Integer.parseInt(matcher.group(1)) - 1,
                            Integer.parseInt(matcher.group(2)) - 1);

            Direction direction =
                    Direction.valueOf(matcher.group(3).toUpperCase());

            logger.trace("successfully parsed rabbit command: " +
                    "Coordinate: " + coordinate + " " +
                    "Direction: " + direction);

            logger.trace(matcher.group());

            RabbitCommand command = new RabbitCommand(coordinate, direction);
            logger.debug("Command coordinate: " + command.coordinate +
                    "\nCommand direction: " + command.direction);
            return command;
        }
        else {
            throw new Exception("Failed to match rabbit command");
        }
    }

    /**
     * Parses Fox input
     * @param line to be parsed and used to attempt a move on a fox
     * @return command with coordinates used to move the fox on the board
     */
    public FoxCommand parseFoxCommand(String line) throws Exception {

        // Slide Fox 1,2 2 left
        // Capture groups
        // 1: Coordinate row : 1
        // 2: Coordinate column : 2
        // 3: MoveSpaces : 2
        // 4: Direction : left

        Matcher matcher = this.regexPatternFox.matcher(line);

        logger.trace("attempting to parse fox command");
        if (matcher.find()) {
            // Extract arguments
            Coordinate coordinate =
                    new Coordinate(Integer.parseInt(matcher.group(1)) - 1,
                            Integer.parseInt(matcher.group(2)) - 1);

            int moveSpaces = Integer.parseInt(matcher.group(3));
            Direction direction =
                    Direction.valueOf(matcher.group(4).toUpperCase());

            logger.trace("successfully parsed fox command: " +
                    "Coordinate: " + coordinate +  " " +
                    "Move Spaces: " + moveSpaces + " " +
                    "Direction: " + direction);
            logger.trace(matcher.group());

            FoxCommand command = new FoxCommand(coordinate, direction,
                    moveSpaces);
            return command;
        }
        else {
            throw new Exception("Failed to match fox command");
        }
    }

    /**
     * Checks if the command is valid
     * @param input
     * @return Rabbit or Fox command
     */
    public Command parseInput(String input) {
        logger.trace("User input received");
        logger.trace(input);

        if (input.length() == 0) {
            throw new IllegalArgumentException("empty " +
                    "input is invalid");
        }

        // Rabbit commands
        try {
            return parseRabbitCommand(input.trim());
        }
        catch (Exception e) {
            logger.trace(e.getMessage());
        }

        // Fox commands
        try {
            return parseFoxCommand(input.trim());
        }
        catch (Exception e) {
            logger.trace(e.getMessage());
        }

        throw new IllegalArgumentException("invalid input");
    }

    /**
     * Generic class for command containing a coordinate and a direction
     *
     */
    public class Command {
        Coordinate coordinate;
        Direction direction;

        public Direction getDirection() {
            return direction;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        protected Command(Coordinate coordinate, Direction direction) {
            this.coordinate = coordinate;
            this.direction = direction;
        }
    }

    /**
     * Command class for the Fox, extends the generic Command class
     * Implementation of the Command class and an additional variable for the spaces to move
     */
    public class FoxCommand extends Command {
        int moveSpaces;

        public int getMoveSpaces() {
            return moveSpaces;
        }

        public void setMoveSpaces(int moveSpaces) {
            this.moveSpaces = moveSpaces;
        }

        public FoxCommand(Coordinate coordinate, Direction direction,
                          int moveSpaces) {
            super(coordinate, direction);
            this.moveSpaces = moveSpaces;
        }
    }

    /**
     * Command class for the Rabbit, extends the generic Command class
     * Implementation of the Command class with no additional variables or methods
     */
    public class RabbitCommand extends Command {

        public RabbitCommand(Coordinate coordinate, Direction direction) {
            super(coordinate, direction);
        }
    }

}
