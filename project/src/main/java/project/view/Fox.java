package project.view;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Class that represents a fox object on the board GUI.
 */
public class Fox extends ButtonBoardItem {

    /**
     * Creates a Fox object to be placed in the board GUI.
     */
    public Fox() {
        super(false);
        BufferedImage image = ImageResources.getInstance().getResources().get("foxHead");
        ImageIcon icon = new ImageIcon(image);
        this.iconButton.setIcon(icon);
    }
}
