package project.view;

import javax.swing.*;
import java.awt.*;

/**
 * Component that creates a circle at the center of a board button
 */
public class Circle extends JComponent {

    private Color color;

    /**
     * Constructor that creates a circle of a certain color
     * @param color
     */
    public Circle (Color color) {
        this.color = color;
    }

    /**
     * Paints the element with the color of the circle
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) { //this is adding green circle at center of button

        super.paintComponent(g);

        int nGap = 10;
        int nXPosition = nGap;
        int nYPosition = nGap;
        int nWidth = getWidth() - nGap * 2;
        int nHeight = getHeight() - nGap * 2;

        g.setColor(this.color);
        g.drawOval(nXPosition, nYPosition, nWidth, nHeight);
        g.fillOval(nXPosition, nYPosition, nWidth, nHeight);


    }
}
