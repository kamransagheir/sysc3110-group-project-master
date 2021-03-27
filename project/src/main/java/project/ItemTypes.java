package project;

public enum ItemTypes {
    EmptyItem("Empty Item"),
    Rabbit("Rabbit"),
    Mushroom("Mushroom"),
    EmptyHole("Empty Hole"),
    EmptyElevated("Empty Elevated position"),
    HoleRabbit("Hole with a Rabbit"),
    ElevatedRabbit("Elevated with a rabbit"),
    HoleMushroom("Hole with a mushroom"),
    ElevatedMushroom("Elevated with a mushroom"),
    Fox("Fox");

    private String type;

    ItemTypes(String s) {
        this.type = s;
    }

    public String getType() { return this.type; };

    public static ItemTypes fromString (String text) {
        for (ItemTypes t : ItemTypes.values()) {
            if (t.type.equalsIgnoreCase(text)) {
                return t;
            }
        }

        throw new IllegalArgumentException("unknown item type");
    }
}
