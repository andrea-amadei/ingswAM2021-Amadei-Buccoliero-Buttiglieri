package it.polimi.ingsw.gamematerials;

import it.polimi.ingsw.model.GameParameters;

import java.util.Objects;

/**
 * The LevelFlag class extends the BaseFlag class and represent a flag, defined by its color and level
 */
public class LevelFlag extends BaseFlag {
    private final int level;

    /**
     * Creates a new flag
     * @param color the color of the flag (taken from the enum FlagColor)
     * @param level the level of the flag (between GameParameters.MIN_CARD_LEVEL and GameParameters.MAX_CARD_LEVEL)
     */
    public LevelFlag(FlagColor color, int level) {
        super(color);

        if(level < GameParameters.MIN_CARD_LEVEL || level > GameParameters.MAX_CARD_LEVEL)
            throw new IllegalArgumentException("Level not valid");

        this.level = level;
    }

    /**
     * @return the level of the flag
     */
    public int getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LevelFlag levelFlag = (LevelFlag) o;
        return level == levelFlag.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }

    @Override
    public String toString() {
        return "LevelFlag{" +
                "color=" + getColor() +
                ", level=" + level +
                '}';
    }
}
