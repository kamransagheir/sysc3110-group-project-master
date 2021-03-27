package project.tui;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.Board;
import project.model.Coordinate;
import project.model.DefaultBoard;
import project.model.GameState;
import project.model.Fox;
import project.solver.Solver;

public class Main {

    /**
     * The Logger used to log useful debug statements and errors.
     */
    public static Logger logger = LogManager.getLogger(Main.class);

    @SuppressWarnings("PMD")
    public static void print(String message) {
        System.out.println(message);
    }

    @SuppressWarnings("PMD")
    public static void warn(String message) {
        System.out.println(ANSIColor.RED + message + ANSIColor.RESET);
    }

    @SuppressWarnings("PMD")
    public static void trace(String message) {
        logger.trace(message);
    }

    @SuppressWarnings("PMD.UseVarargs")
    public static void main(String[] args) {
        Solver solver = new Solver();

        print("Starting JumpIn");
        DefaultBoard defBoard = new DefaultBoard();
        Board board = defBoard.getBoard();
        logger.debug(board.currentGameState);
        logger.debug(board.getItem(new Coordinate(0,3)));
        logger.debug(board.getItem(new Coordinate(4,2)));
        logger.debug(board.getItem(new Coordinate(1,1)));
        Fox fox = (Fox) board.getItem(new Coordinate(1,1));
        logger.debug(fox.orientation);
        logger.debug(board.getItem(new Coordinate(3,2)));
        // JumpIn client
        Scanner scanner = new Scanner(System.in);

        JumpInClient client = new JumpInClient();

        while (board.currentGameState == GameState.IN_PROGRESS) {

            try {
                print(client.getPrompt(board));

                String userInput = scanner.nextLine();
                JumpInClient.Command command = client.parseInput(userInput);

                if (command instanceof JumpInClient.RabbitCommand) {
                    board = board.jump(command.direction, command.coordinate);
                }

                if (command instanceof JumpInClient.FoxCommand) {
                    logger.trace("In Fox");
                    JumpInClient.FoxCommand foxCommand =
                            (JumpInClient.FoxCommand) command;

                    logger.trace("Parsed Command");
                    board = board.slide(foxCommand.direction,
                            foxCommand.moveSpaces,
                            foxCommand.coordinate);
                    logger.trace("No error");
                }
                solver.solve(board);
            }
            catch(Exception e) {
                logger.error(e);
            }

        }

        scanner.close();
       // board.getCurrentGameState();
        print(board.toString());
        print(ANSIColor.GREEN + "Game has been solved " +
                "successfully!" + ANSIColor.RESET);

    }

}
