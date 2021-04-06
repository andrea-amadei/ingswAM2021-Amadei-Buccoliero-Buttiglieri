package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.production.Crafting;

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

    //activate function
    @Override
    public void activate(Player player) {

        //TODO: activate function

    }

    @Override
    public String toString() {
        return "CraftingAbility{" +
                "crafting=" + crafting +
                '}';
    }
}
