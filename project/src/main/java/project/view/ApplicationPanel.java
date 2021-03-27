package project.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 
 * GuiOuterFrame deals with the initialization of the the GUI board outer
 * components including the JToolBar i.e., menu bar. Makes call to the class
 * GuiInnerComponents initialize and render the inner components of GUI
 * 
 *
 */
public class ApplicationPanel extends JPanel {

	/**
	 * Used for visual layout
	 */
	private static final int PADDING = 3;

	private Board boardView;

	public ApplicationPanel(ToolBar toolBar,
							ItemClickListener listener,
							project.model.Board board) {

		super(new BorderLayout(PADDING, PADDING));
		this.setBorder(new EmptyBorder(PADDING, PADDING, PADDING,
				PADDING));

		// Create board
		boardView = new Board(listener, board);

		// Add board and toolbar
		this.add(toolBar, BorderLayout.PAGE_START);
		this.add(boardView);
	}

	public void setBoard(project.model.Board board) {
		this.boardView.updateBoard(board);
	}

}
