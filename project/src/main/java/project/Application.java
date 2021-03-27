package project;

import com.google.common.base.Optional;
import io.atlassian.fugue.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.*;
import project.model.Fox;
import project.solver.Move;
import project.solver.Solver;
import project.view.*;
import project.view.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * GUI Application for JumpIn
 * This is the entrypoint for the GUI game
 */
public class Application extends JFrame implements ItemClickListener {

    /**
     * The logger used to log errors.
     */
    public static Logger logger = LogManager.getLogger(Board.class);

    private ApplicationMode applicationMode;

    /**
     * State of the board
     */
    private project.model.Board board;
    /**
     * Root GUI component
     */
    private ApplicationPanel frame;

    /**
     * The history of boards in this application.
     */
    private BoardHistory boardHistory;

    /**
     * Holds the buttons and label
     */
    private ToolBar toolBar;

    /**
     * Stores first and second selection, when playing games or setting foxes
     */
    private Optional<Coordinate> selectedItem;
    /**
     * The coordinates where the selected item will go.
     */
    private Optional<Coordinate> destinationItem;

    private String levelBuilderMessage = "Select an item to change";
    private String gameMessage = "Make your move!";


    /**
     * Action that creates a new game.
     */
    private Action newGame = new AbstractAction("New") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            newGame();
        }
    };

    private Action switchMode = new AbstractAction("Switch modes") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (applicationMode == ApplicationMode.GAME_PLAY) {
                initializeBuilder();
                setMessage(levelBuilderMessage);
            } else if (applicationMode == ApplicationMode.LEVEL_BUILDER) {
                initializeGame();
                setMessage(gameMessage);
            }
            updateBoard();
        }
    };

    /**
     * Action that undoes the current move.
     */
    private Action undo = new AbstractAction("Undo") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (applicationMode != ApplicationMode.GAME_PLAY) {
                showError("Cannot undo in level builder");
                return;
            }

            undo();
        }
    };

    /**
     * Action that redoes the current move.
     */
    private Action redo = new AbstractAction("Redo") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (applicationMode != ApplicationMode.GAME_PLAY) {
                showError("Cannot redo in level builder");
                return;
            }
            redo();
        }
    };

    /**
     * Action that saves the current board.
     */
    private Action save = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            save();
        }
    };

    /**
     * Action that loads the current board.
     */
    private Action load = new AbstractAction("Load") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            load();
        }
    };

    /**
     * Action that solves the current board.
     */
    private Action solve = new AbstractAction("solve") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            solve();
        }
    };

    /**
     * Solves the game.
     */
    private void solve() {
        if (applicationMode != ApplicationMode.LEVEL_BUILDER) {
            showError("Solver button is meant for checking level builder, not" +
                    " for the game");
            return;
        }
        int defaultTimeSeconds = 30;

        final ExecutorService service = Executors.newSingleThreadExecutor();

        showMessage("attempting to solve the board");

        try {
            final Future<List<Move>> f = service.submit(() -> {
                return Solver.solve(board);
            });

            List<Move> moves = f.get(defaultTimeSeconds, TimeUnit.SECONDS);
            logger.debug("solved in time");
            showMessage("This level can be solved.");
        } catch (final TimeoutException e) {
            showError("the board could not be solved in the default settings," +
                    " it may not be solvable");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }

    }

    /**
     * Creates a new game.
     */
    private void newGame() {
        initializeGame();
        setMessage("Make your move!");
        updateBoard();
    }

    /**
     * Creates the application, initializing the board and the frame.
     */
    private Application() {
        super("JumpIn");

        // Ensure resources are loaded;
        ImageResources.getInstance();

        this.selectedItem = Optional.absent();
        this.destinationItem = Optional.absent();

        initializeGame();
        initializeFrame();
        setMessage(gameMessage);
    }

    /**
     * Initializes the game with a new board and boardhistory
     */
    private void initializeGame() {
        this.applicationMode = ApplicationMode.GAME_PLAY;
        this.board = new DefaultBoard().getBoard();
        this.boardHistory = new BoardHistory(this.board);
    }

    private void initializeBuilder() {
        this.applicationMode = ApplicationMode.LEVEL_BUILDER;
        this.board = new DefaultBoard().getBoard();
        this.boardHistory = new BoardHistory(this.board);
    }

    /**
     * Initializes the frame of the gui.
     */
    private void initializeFrame() {

        toolBar = new ToolBar(this.newGame, this.solve, this.undo, this.redo,
            this.save, this.load,
                this.switchMode, this.applicationMode);

        // GUI Components
        frame = new ApplicationPanel(toolBar, this, this.board);

        // Frame
        this.add(frame);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Whether to use the native windowing systems default location
        this.setLocationByPlatform(true);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Undoes the last move on the board.
     */
    private void undo() {
		project.model.Board recalledMove = boardHistory.getUndoBoard();
		updateBoard(recalledMove);
    }

    /**
     * Redoes the last move that was undone.
     */
    private void redo() {
        project.model.Board recalledMove = boardHistory.getRedoBoard();
        updateBoard(recalledMove);
    }

    /**
     * Saves the current board into a filename given by the user.
     */
    private void save(){
        try {
            String filename = JOptionPane.showInputDialog("What would you like to call your board");
            if (filename.equals("null") || filename.equals("")) {
                filename = "defaultSaveFile";
            }

            if (!(filename == null)) {
                XMLParser.writeToXMLFile(board, filename);
            }
        }
        catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    /**
     * Loads the board contained in a filename given by the user.
     */
    private void load(){
        try{
            String filename = JOptionPane.showInputDialog("What board would you like to load?");
            if (filename.equals("null") || filename.equals("")) {
                filename = "defaultSaveFile";
            }

            if (!(filename == null)) {
                board = XMLParser.boardFromXML(filename);
                boardHistory = new BoardHistory(board);
            }
            
            updateBoard();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(
                    null, "This file was not found.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the board displayed by the gui.
     */
    private void updateBoard () {
        updateBoard(this.board);
    }

    /**
     * Updates the board using a given board.
     * @param newBoard The board that the gui should be updated to model.
     */
    private void updateBoard (project.model.Board newBoard) {
        this.board = newBoard;
        this.frame.setBoard(newBoard);

        if (board.currentGameState == GameState.SOLVED) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showMessageDialog(this, "Congratulations! \nYou Won!",
                    "Victory!", 1);
            int playAgain = JOptionPane.showConfirmDialog(this, "Do you want " +
                    "to play again?", "Reset?", dialogButton);
            if (playAgain == JOptionPane.YES_OPTION) {
                logger.debug("Restart game");
				newGame();
            }
        }
    }

    /**
     * This method handles level edits to positions that are currently a fox
     * @param coordinate
     */
    private void onItemClickBuilderFox(Coordinate coordinate) {
        logger.trace("clear the fox off the board");

        BoardItem currentItem = (Fox)
                board.getItem(coordinate);

        Coordinate left =
                currentItem.coordinate.right().get().left();
        Coordinate right =
                currentItem.coordinate.right().get().right();

        // Set both spots to empty
        this.board = this.board.setItem(new EmptyBoardItem(left));
        this.board = this.board.setItem(new EmptyBoardItem(right));


    }

    /**
     * Sends an event when an item on the gui is clicked.
     * @param event The event sent by the clicked item.
     */
    @Override
    public void onItemClick(ItemClickEvent event) {

        logger.debug("Received an item click");
        if (applicationMode == ApplicationMode.GAME_PLAY) {
            onItemClickGamePlay(event);
        }
         else if (applicationMode == ApplicationMode.LEVEL_BUILDER) {
             onItemClickBulider(event);
        }
    }

    private void onItemClickBulider(ItemClickEvent event) {
        try {
            Optional<BoardItem> changedItem = ItemSelection.show(this,
                    event.coordinate);

            // It is not a fox
            if (changedItem.isPresent()) {

                logger.trace("User chose an item to set");
                // Trying to replace a fox
                if (board.getItem(event.coordinate) instanceof Fox) {
                    logger.trace("trying to replace a fox");
                    this.onItemClickBuilderFox(event.coordinate);
                }

                logger.trace("setting item");

                // Set the new item
                this.board = board.setItem(changedItem.get());
                this.updateBoard(board);
            }
            // It is a Fox
            else {
                // Selecting head
                if (!selectedItem.isPresent()) {
                    // Set the new item when it is as fox
                    // Ask for a second coordinate
                    logger.trace("tried to set a fox, asking for second " +
                            "coordinate");

                    setMessage("Please select the tail of the fox you want " +
                            "to place");

                    selectedItem = Optional.of(event.coordinate);
                    destinationItem = Optional.absent();
                }


                // Selecting tail
                else {
                    // Second click
                    // Set fox tail coordinate
                    logger.trace("got a second click");

                    destinationItem = Optional.of(event.coordinate);

                    // Try to create the fox
                    Coordinate head = selectedItem.get();
                    Coordinate tail = destinationItem.get();

                    // Clear items
                    selectedItem = Optional.absent();
                    destinationItem = Optional.absent();

                    try {
                        logger.trace("fox coordinates", head, tail);
                        Fox fox = new Fox(Pair.pair(head, tail));
                        logger.trace("trying to set a new fox");
                        this.board = board.setItem(fox);
                        this.updateBoard(board);
                        logger.trace("set a new fox");
                    } catch (IllegalArgumentException e) {
                        logger.debug(e);
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Exception!"
                                , 0);

                    }
                }
            }
        } catch (IllegalArgumentException e) {
            logger.debug("failed to set an item");
        }
    }

    private void onItemClickGamePlay(ItemClickEvent event) {

        setMessage(levelBuilderMessage);
        // We haven't selected an item
        if (!selectedItem.isPresent()) {

            if (!board.isMovable(event.coordinate)) {
                logger.debug("Non-movable selected");
                return;
            }
            logger.trace("Selected item at coordinate " + event.coordinate);
            selectedItem = Optional.of(event.coordinate);
            return;
        }

        // We have selected an item
        else {
            // If this is the same item already selected
            if (event.coordinate.equals(selectedItem.get())) {
                logger.trace("Tried to select the same item twice");
                return;
            }

            logger.trace("Destination item at coordinate " + event.coordinate);

            destinationItem = Optional.of(event.coordinate);

            // Apply to the board
            try {
                project.model.Board appliedBoard = this.board.move(selectedItem.get(),
                        destinationItem.get());
                this.boardHistory.addState(appliedBoard);
                updateBoard(appliedBoard);
            } catch (InvalidMoveException e) {
                logger.debug(e);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Exception!"
                        , 0);
            } finally {
                // Clear selections
                selectedItem = Optional.absent();
                destinationItem = Optional.absent();
            }
        }

    }

    /**
     * Sets the message in the toolbar.
     * @param msg The message that the toolbar should contain.
     */
    private void setMessage(String msg) {
        toolBar.setMessage(msg);
        pack();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Exception!"
                , JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Exception!"
                , JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("PMD")
    public static void main(String[] args) {
        new Application();
    }
}
