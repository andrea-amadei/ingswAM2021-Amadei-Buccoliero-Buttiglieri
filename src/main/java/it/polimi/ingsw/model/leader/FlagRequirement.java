package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.BaseFlag;
import it.polimi.ingsw.model.Player;

/**
 * Class FlagRequirement implements Requirement Interface
 * represents the requirement of a flag with no levels
 */
public class FlagRequirement implements Requirement{

    private final BaseFlag flag;
    private final int amount;

    /**
     * FlagRequirement Constructor
     * @param flag the type of flag required
     * @param amount the amount of flags of said type required
     * @throws IllegalArgumentException if negative or null amount of flags
     * @throws NullPointerException if flag pointer is null
     */
    public FlagRequirement(BaseFlag flag, int amount){

        if(amount <= 0)
            throw new IllegalArgumentException("Amount of flags required" +
                                            " cannot be negative or zero");

        if(flag == null)
            throw new NullPointerException();

        this.amount = amount;
        this.flag = flag;
    }

    /**
     * get flag required
     * @return the pointer to required flag
     */
    public BaseFlag getFlag() {
        return flag;
    }

    /**
     * get amount of required flags
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
