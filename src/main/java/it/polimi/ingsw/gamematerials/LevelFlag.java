package it.polimi.ingsw.gamematerials;

import it.polimi.ingsw.model.GameParameters;

public class LevelFlag extends BaseFlag {
    private final int level;

    public LevelFlag(FlagColor color, int level) {
        super(color);

        if(level < GameParameters.MIN_CARD_LEVEL || level > GameParameters.MAX_CARD_LEVEL)
            throw new IllegalArgumentException("Level not valid");

        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
