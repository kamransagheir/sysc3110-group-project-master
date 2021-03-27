package project.view;

import java.awt.*;

/**
 * Represents an Elevated item, which is a darker color and can contain another item.
 */
public class Elevated extends ContainerItem {
    private static Color color = GuiColor.DARK_GREEN;

    /**
     * Creates the elevated board object.
     * @param model The Elevated model object that the gui item is representing.
     */
    public Elevated(project.model.ContainerItem model) {
        super(model, color);
    }
}
