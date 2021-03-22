package it.polimi.ingsw.gamematerials;

/**
 * The BaseFlag class represents a generic flag, only defined by its color
 */
public class BaseFlag {
    private final FlagColor color;

    /**
     * Creates a new generic flag
     * @param color the color of the flag
     */
    public BaseFlag(FlagColor color) {
        this.color = color;
    }

    /**
     * @return the color of the flag
     */
    public FlagColor getColor() {
        return color;
    }
}
