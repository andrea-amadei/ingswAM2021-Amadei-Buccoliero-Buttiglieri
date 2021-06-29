package it.polimi.ingsw.server.model.leader;

import it.polimi.ingsw.server.model.basetypes.BaseFlag;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.common.parser.raw.RawRequirement;

import java.util.Map;
import java.util.Set;

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
            throw new IllegalArgumentException("Amount of flags required cannot be negative or zero");

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

    /**
     * method verifies that the player has enough base flags to satisfy the requirement
     * @param player the player who is being verified
     * @return true iff the player satisfies the requirements
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public boolean isSatisfied(Player player) {

        if(player == null)
            throw new NullPointerException();

        int total = 0;
        Set<Map.Entry<LevelFlag, Integer>> entrySet = player.getBoard().getFlagHolder().getFlags().entrySet();
        for(Map.Entry<LevelFlag, Integer> m : entrySet) {
            if(m.getKey().getColor().equals(flag.getColor()))
                total += m.getValue();
        }
        return total >= amount;

    }

    /**
     * function represents the requirement as a string
     * @return the flag requirement as a string
     */
    @Override
    public String toString() {
        return "FlagRequirement{" +
                "flag=" + flag +
                ", amount=" + amount +
                '}';
    }

    @Override
    public RawRequirement toRaw() {
        return new RawRequirement(this);
    }
}
