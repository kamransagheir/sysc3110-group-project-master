package project.model;

import org.junit.jupiter.api.Test;
import project.tui.JumpInClient;

import static org.junit.jupiter.api.Assertions.*;

public class JumpInClientTest {

    @Test
    /**
     *  Test rabbit jumping
     */
    void testRabbitJump() {
        JumpInClient client = new JumpInClient();
        String userInput = "Jump Rabbit 1,1 Up";

        try {
            JumpInClient.Command command = client.parseRabbitCommand(userInput);

            assertTrue(command instanceof JumpInClient.RabbitCommand, "should be" +
                    " a rabbit command");

            JumpInClient.RabbitCommand rabbitCommand =
                    (JumpInClient.RabbitCommand) command;

            assertEquals(rabbitCommand.getCoordinate(), new Coordinate(0,0),
                    "coordinate should be same as input");
            assertEquals(rabbitCommand.getDirection(), Direction.UP, "direction should" +
                    "be same as input");

        } catch (Exception e) {
            fail("Exception was thrown");
        }
    }

    @Test
    /**
     *  Test rabbit jumping
     */
    void testRabbitJumpBadWhiteSpace() {
        JumpInClient client = new JumpInClient();
        String userInput = "Jump    Rabbit    1,1   Up";

        try {
            client.parseRabbitCommand(userInput);
        } catch (Exception e) {
            fail("Exception was thrown");
        }
    }

    @Test
    /**
     *  Test fox sliding
     */
    void testFoxSlide() {
        String userInput = "Slide Fox 1,1 2 Up";
        JumpInClient client = new JumpInClient();

        try {
            JumpInClient.Command command = client.parseFoxCommand(userInput);
            assertTrue(command instanceof JumpInClient.FoxCommand, "should be" +
                    " a fox command");

            JumpInClient.FoxCommand foxCommand = (JumpInClient.FoxCommand) command;

            assertEquals(foxCommand.getCoordinate(), new Coordinate(0,0),
                    "coordinate should be same as input");
            assertEquals(foxCommand.getDirection(), Direction.UP, "direction should" +
                    "be same as input");
            assertEquals(foxCommand.getMoveSpaces(), 2, "move spaces should be " +
                    "same as input");
        } catch (Exception e) {
            fail("Exception was thrown");
        }
    }

    @Test
    /**
     * Mix up input ie. Slide Rabbit, Jump Fox.
     */
    void testParserWithMixedInput () {
        JumpInClient client = new JumpInClient();

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Slide Rabbit 1,1 2 Up");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Slide Rabbit 1,1 2 Up");
            command.toString();
        });

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Jump Fox 1,1 2 Up");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Jump Fox 1,1 2 Up");
            command.toString();
        });

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Slide Rabbit 1,1 Left");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Slide Rabbit 1,1 Left");
            command.toString();
        });

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Jump Fox 1,1 Right");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Jump Fox 1,1 Right");
            command.toString();
        });
    }

    @Test
    /**
     * Invalid input. Not Fox or Rabbit
     */
    void testParserWithWrongInput () {
        JumpInClient client = new JumpInClient();

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Slide Gorilla 1,1 2 Up");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Slide Gorilla 1,1 2 Up");
            command.toString();
        });

        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseFoxCommand("Jump Gorilla 1,1 Up");
            command.toString();
        });
        assertThrows(Exception.class, () -> {
            JumpInClient.Command command = client.parseRabbitCommand("Jump Gorilla 1,1 Up");
            command.toString();
        });
    }
}
