package project.view;

import project.model.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Implements the inner part of the gui, containing the board.
 */
public class Board extends JPanel {

	/**
	 * Listens for events sent by items.
	 */
	private ItemClickListener listener;

	/**
	 * The model that the board gui is based on.
	 */
	private project.model.Board board;

	/**
	 * Creates a gui for the inner components using an initial board state.
	 */
	public Board(ItemClickListener listener, project.model.Board board) {
		super(new GridLayout(board.numberOfRows,
				board.numberOfColumns));

		this.listener = listener;
		this.board = board;

		this.setBorder(new LineBorder(Color.BLACK));
		this.setLayout(new GridLayout(board.numberOfRows,
				board.numberOfColumns + 1));

		render();
	}

	/**
	 * Renders the gui with the model of the board.
	 */
	private void render() {

		// Clear GUI State
		this.removeAll();

		// Rebuild GUI
		for (int row = 0; row < board.numberOfRows; row++) {
			for (int column = 0; column < board.numberOfColumns; column++) {
				BoardItem modelItem = board.getItem(new Coordinate(row, column));
				JComponent viewItem = new GUIBoardItem(new Coordinate(row,
						column), modelItem, this.listener);
				this.add(viewItem);
			}
		}

		// Render changes
		repaint();
		revalidate();
	}

	/**
	 * Updates the board with a new model to render.
	 * @param board The board model to render.
	 */
	public void updateBoard(project.model.Board board) {
		this.board = board;

		render();
	}
}
