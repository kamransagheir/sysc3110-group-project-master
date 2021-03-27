package project.view;

import project.model.Mushroom;
import project.model.Rabbit;

import javax.swing.*;
import java.awt.*;

/**
 * Class that represents an item on the board GUI that can contain another item.
 */
public abstract class ContainerItem extends JPanel {

    /**
     * Creates an item that can contain another item on the board.
     * @param model The ContainerItem object that the item in the GUI represents.
     * @param color The desired color of the item.
     */
    public ContainerItem(project.model.ContainerItem model, Color color) {
        this.setLayout(new OverlayLayout(this));
        this.setOpaque(false);
        this.setBackground(Color.green);
        //check if empty
        if (model.containingItem.isPresent()) {
            project.model.Containable containable = model.containingItem.get();
            if (containable instanceof Rabbit) {
                this.add(new project.view.Rabbit(false));
            }
            if (containable instanceof Mushroom) {
                this.add(new project.view.Mushroom(false));
            }
        }
        Circle circle = new Circle(color);
        this.add(circle);
    }
}
