package project.view;

import javax.swing.*;

/**
 * Abstract class representing a board item's button on the GUI.
 */
public abstract class ButtonBoardItem extends JPanel {
    protected JButton iconButton;

    /**
     * Creates a button item on the board.
     * @param renderItem True if a background circle should be added to the button rendering.
     */
    public ButtonBoardItem(boolean renderItem) {
        this.setLayout(new OverlayLayout(this));
        iconButton = new JButton();
        this.iconButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        this.iconButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        this.iconButton.setOpaque(false);
        this.iconButton.setContentAreaFilled(false);
        this.iconButton.setBorderPainted(false);
        this.iconButton.setBackground(GuiColor.VERY_DARK_GREEN);
        this.add(iconButton);
        if (renderItem) {
            Circle circle = new Circle(GuiColor.VERY_DARK_GREEN);
            this.add(circle);
        }
        this.setOpaque(false);
    }
}
