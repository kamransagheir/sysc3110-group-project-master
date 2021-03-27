package project.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class representing the JPanel gui and associated model item for each item on the board.
 */
public class GUIBoardItem extends JPanel implements ActionListener, MouseListener{
    public static Logger logger = LogManager.getLogger(GUIBoardItem.class);

    private project.model.BoardItem item;

    ItemClickListener listener;
    private Coordinate coordinate;

    /**
     * Creates a GUI item with its associated board object.
     * @param coordinate The coordinate of the board item.
     * @param item The BoardItem represented by the JPanel.
     * @param listener That item's ItemClickListener.
     */
    public GUIBoardItem(Coordinate coordinate, BoardItem item,
                        ItemClickListener listener) {
        this.listener = listener;
        this.item = item;
        this.setBackground(GuiColor.DARK_GREEN);

        this.setLayout(new OverlayLayout(this));

        if (item instanceof project.model.Rabbit) {
            this.coordinate = coordinate;
            this.add(new Rabbit());
        }
        else if (item instanceof project.model.Mushroom) {
            this.coordinate = coordinate;
            this.add(new Mushroom());
        }
        else if (item instanceof project.model.Hole) {
            this.coordinate = coordinate;
            project.model.Hole hole = (project.model.Hole) item;
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
            this.add(new Hole(hole));
        }

        else if (item instanceof project.model.ElevatedBoardItem) {
            this.coordinate = coordinate;
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
            ElevatedBoardItem elevatedBoardItem = (ElevatedBoardItem) item;
            this.add(new Elevated(elevatedBoardItem));
        }
        else if (item instanceof EmptyBoardItem) {
            this.coordinate = coordinate;
            Circle circle = new Circle(GuiColor.VERY_DARK_GREEN);
            this.add(circle);
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        }
        else if (item instanceof project.model.Fox)  {
            this.coordinate = coordinate;
            this.add(new Fox());
        }

        else {
            logger.error("Unsupported type" + item);
        }

        this.setBackground(GuiColor.DARK_GREEN);
        this.setSize(200,200);
        this.setListeners(this);
    }

    /**
     * Sets the listeners for each item in a Jcomponent containing items.
     * @param parent The JComponent containing items that need listeners.
     */
    public void setListeners(JComponent parent) {
        Component children[] = parent.getComponents();

        if (!(children.length == 0)) {
            for (Component child : children) {
                if (child instanceof JComponent) {
                    child.addMouseListener(this);
                    this.setListeners((JComponent) child);
                }
            }
        }
    }

    /**
     * Sends a new click event at this item's coordinate.
     */
    public void sendEvent() {
        ItemClickEvent event = new ItemClickEvent(this.coordinate);
        logger.trace("click event generated" + item);
        this.listener.onItemClick(event);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.sendEvent();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        this.sendEvent();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        this.sendEvent();
    }

    //methods below required because of implementing MouseListener
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
