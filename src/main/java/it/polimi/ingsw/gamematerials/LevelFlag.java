package it.polimi.ingsw.gamematerials;

public class LevelFlag extends BaseFlag {
    private final int level;

    public LevelFlag(FlagColor color, int level) {
        super(color);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
