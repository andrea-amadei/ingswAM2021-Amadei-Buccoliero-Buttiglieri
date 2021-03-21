package it.polimi.ingsw.gamematerials;

public class LevelFlag extends BaseFlag {
    private final int level;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 3;

    public LevelFlag(FlagColor color, int level) {
        super(color);

        if(level < MIN_LEVEL || level > MAX_LEVEL)
            throw new IllegalArgumentException("Level not valid");

        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
