package project.view;

import java.awt.*;

/**
 * Item representing a Hole object in the GUI.
 */
public class Hole extends ContainerItem {
    private static Color color = GuiColor.BROWN;

    /**
     * Creates a hole object.
     * @param model The Hole model object that is being represented in the gui.
     */
    public Hole(project.model.ContainerItem model) {
        super(model, color);
    }
}
