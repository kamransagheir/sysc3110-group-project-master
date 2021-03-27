package project.view;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Represents a mushroom object placed in the GUI.
 */
public class Mushroom extends ButtonBoardItem {

    /**
     * Creates a mushroom object with a specification of whether the dark circle should be rendered on the background.
     * @param renderBackground True if the green circle is rendered in the background.
     */
    public Mushroom(boolean renderBackground) {
        super(renderBackground);
        BufferedImage image = ImageResources.getInstance().getResources().get("mushroom");
        ImageIcon icon = new ImageIcon(image);
        this.iconButton.setIcon(icon);
    }

    /**
     * Creates a mushroom item with its background rendered.
     */
    public Mushroom() {
        this(true);
    }
}
