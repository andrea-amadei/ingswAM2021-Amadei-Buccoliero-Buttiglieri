package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;

/**
 * Class CraftingAbility implements SpecialAbility interface
 * represents leader card ability to add a new crafting scheme to the player's available
 * crafting schemes
 */
public class CraftingAbility implements SpecialAbility {

    private final Crafting crafting;

    /**
     * CraftingAbility Constructor
     * @param crafting the crafting to assign as a special ability
     * @throws NullPointerException if crafting pointer is null
     */
    public CraftingAbility (Crafting crafting){

        if(crafting == null)
            throw new NullPointerException();

        this.crafting = crafting;

    }

    /**
     * get crafting
     * @return the crafting
     */
    public Crafting getCrafting(){
        return crafting;
    }

    /**
     * function activates the leader ability by adding a new crafting to the player's available
     * crafting
     * @param player the player who activates the leader card
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public void activate(Player player) {
        if(player == null)
            throw new NullPointerException();
        player.getBoard().getProduction().addLeaderCrafting(crafting);
    }

    /**
     * function represents the ability as a string
     * @return the crafting ability as a string
     */
    @Override
    public String toString() {
        return "CraftingAbility{" +
                "crafting=" + crafting +
                '}';
    }

    @Override
    public RawSpecialAbility toRaw() {
        return new RawSpecialAbility(this);
    }
}
