package it.polimi.ingsw.server.model.leader;

import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.common.parser.raw.RawRequirement;

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

    /**
     * method verifies that the player has enough level flags to satisfy the requirement
     * @param player the player who is being verified
     * @return true iff the player satisfies the requirements
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public boolean isSatisfied(Player player) {

        if(player == null)
            throw new NullPointerException();

        Integer availableAmount = player.getBoard().getFlagHolder().getFlags().get(flag);
        if (availableAmount == null)
            return false;
        return availableAmount >= amount;

    }

    /**
     * function represents the requirement as a string
     * @return the level flag requirement as a string
     */
    @Override
    public String toString() {
        return "LevelFlagRequirement{" +
                "flag=" + flag +
                ", amount=" + amount +
                '}';
    }

    @Override
    public RawRequirement toRaw() {
        return new RawRequirement(this);
    }
}
