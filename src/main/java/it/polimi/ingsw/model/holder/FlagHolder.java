package it.polimi.ingsw.model.holder;

import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;

import java.util.HashMap;
import java.util.Map;

/**
 * The class FlagHolder contains all the flags earned by a player.
 */
public class FlagHolder {
    private final Map<LevelFlag, Integer> flags;

    /**
     * Creates a Flag Holder
     */
    public FlagHolder() {
        flags = new HashMap<>();
    }

    /**
     * @return a Map of all flags owned and their respective amount
     */
    public Map<LevelFlag, Integer> getFlags() {
        return new HashMap<>(flags);
    }

    /**
     * Adds a given flag to the flag holder
     * @param flag the flag to be added
     * @throws NullPointerException if flag is null
     */
    public void addFlag(LevelFlag flag) {
        if(flag == null)
            throw new NullPointerException();

        if(flags.containsKey(flag))
            flags.put(flag, flags.get(flag) + 1);
        else
            flags.put(flag, 1);
    }

    /**
     * Counts the number of owned flags of a given kind
     * @param flag the flags to count
     * @return the number of flags owned matching flag
     * @throws NullPointerException if flag is null
     */
    public int numberOfFlags(LevelFlag flag) {
        if(flag == null)
            throw new NullPointerException();

        int n;

        try {
            n = flags.get(flag);
        }
        catch (NullPointerException e) {
            n = 0;
        }

        return n;
    }

    /**
     * Counts the number of owned flags with a given color
     * @param color the color of the flags to count
     * @return the number of flags owned matching the color
     * @throws NullPointerException if color is null
     */
    public int numberOfFlagsByColor(FlagColor color) {
        if(color == null)
            throw new NullPointerException();

        int n = 0;

        for(LevelFlag i : flags.keySet())
            if(i.getColor().equals(color))
                n += flags.get(i);

        return n;
    }

    /**
     * Counts the number of owned flags with a given level
     * @param level the level of the flags to count
     * @return the number of flags owned matching the level
     * @throws IllegalArgumentException if level is not positive
     */
    public int numberOfFlagsByLevel(int level) {
        if(level <= 0)
            throw new IllegalArgumentException("Flag level must be above 0");

        int n = 0;

        for(LevelFlag i : flags.keySet())
            if(i.getLevel() == level)
                n += flags.get(i);

        return n;
    }

    /**
     * Clears all flags
     */
    public void reset() {
        flags.clear();
    }
}
