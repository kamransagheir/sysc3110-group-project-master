package project.view;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Represents a rabbit object placed on the GUI.
 */
public class Rabbit extends ButtonBoardItem {

    /**
     * Creates a rabbit object, and optionally renders a background circle behind it.
     * @param renderBackground True if the background circle should be rendered.
     */
    public Rabbit(boolean renderBackground) {
        super(renderBackground);
        BufferedImage image = ImageResources.getInstance().getResources().get("brownRabbit");
        ImageIcon icon = new ImageIcon(image);
        this.iconButton.setIcon(icon);
    }

    /**
     * Creates a rabbit with a rendered background circle.
     */
    public Rabbit() {
        this(true);
    }
}
