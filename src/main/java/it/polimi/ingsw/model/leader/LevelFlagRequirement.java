package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.model.Player;

/**
 * Class LevelFlagRequirement implements Requirement Interface
 * represents the requirement of a flag with a set level
 */
public class LevelFlagRequirement implements Requirement{

    private final LevelFlag flag;
    private final int amount;

    /**
     * LevelFlagRequirement Constructor
     * @param flag the type of flag (with level) required
     * @param amount the amount of flags of said type required
     * @throws IllegalArgumentException if negative or null amount of flags
     * @throws NullPointerException if flag pointer is null
     */
    public LevelFlagRequirement(LevelFlag flag, int amount){

        if(amount <= 0)
            throw new IllegalArgumentException("Amount of flags required cannot be negative or zero");

        if(flag == null)
            throw new NullPointerException();

        this.amount = amount;
        this.flag = flag;
    }

    /**
     * get flag required
     * @return the pointer to the required flag
     */
    public LevelFlag getFlag() {
        return flag;
    }

    /**
     * get amount of flags required
     * @return the amount of flags required
     */
    public int getAmount() {
        return amount;
    }

    //is satisfied function
    @Override
    public boolean isSatisfied(Player player) {

        //TODO: isSatisfied function
        return false;
    }
}
