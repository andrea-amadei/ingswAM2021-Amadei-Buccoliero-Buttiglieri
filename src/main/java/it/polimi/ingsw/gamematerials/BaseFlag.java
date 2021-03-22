package it.polimi.ingsw.gamematerials;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseFlag baseFlag = (BaseFlag) o;
        return color == baseFlag.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
