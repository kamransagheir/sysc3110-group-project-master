package project.view;

/**
 * Interface for items that can be clicked, triggering an ItemClickEvent
 */
public interface ItemClickListener {
    /**
     * Method that handles an ItemClickEvent
     * @param event The event sent by the clicked item
     */
    void onItemClick (ItemClickEvent event);
}
