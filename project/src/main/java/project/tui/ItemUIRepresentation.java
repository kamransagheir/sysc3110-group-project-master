package project.tui;

import project.tui.ANSIColor;

/**
 * Enum containing a UI representation for each item, usually first character(s) of the item's name.
 */
public enum ItemUIRepresentation {

    EMPTY(ANSIColor.GREEN + "E" + ANSIColor.RESET),
    MUSHROOM(ANSIColor.PURPLE + "M" + ANSIColor.RESET),
    FOX(ANSIColor.RED + "F" + ANSIColor.RESET),
    HOLE_EMPTY(ANSIColor.CYAN + "HE" + ANSIColor.RESET),
    HOLE_OCCUPIED_RABBIT(ANSIColor.BLUE + "HR" + ANSIColor.RESET),
    HOLE_MUSHROOM(ANSIColor.BLUE + "HM" + ANSIColor.RESET),
    ELEVATED(ANSIColor.WHITE + "U" + ANSIColor.RESET),
    ELEVATED_MUSHROOM(ANSIColor.PURPLE + "UM" + ANSIColor.RESET),
    ELEVATED_RABBIT(ANSIColor.YELLOW + "UR" + ANSIColor.RESET),
    RABBIT(ANSIColor.YELLOW + "R" + ANSIColor.RESET);

    /**
     * The representation, if it is specified by the user.
     */
    private String representation;

    /**
     * Creates a specific UI representation.
     * @param s The string containing the given UI representation
     */
    ItemUIRepresentation(String s){
        this.representation = s;
    }

    /**
     * Gets the UI representation.
     * @return The string containing the UI representation.
     */
    public String getRepresentation() {
        return this.representation;
    }
}
