package it.polimi.ingsw.model.production;

import it.polimi.ingsw.model.GameParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * The production class defines the methods to interact with every crafting aspect of the game.
 * The class holds 3 lists of different types of crafting:
 *      - Base crafting are granted to everyone at the start of the game
 *      - Leader crafting are added by the some of the leaders' abilities
 *      - Upgradable crafting can be purchased or upgraded from the shop
 */
public class Production {
    private final List<Crafting> baseCrafting;
    private final List<Crafting> leaderCrafting;
    private final List<UpgradableCrafting> upgradableCrafting;

    /**
     * Creates a new production instance.
     * Set every crafting slot to empty.
     */
    public Production() {
        baseCrafting = new ArrayList<>();
        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>(GameParameters.UPGRADABLE_CRAFTING_NUMBER);

        for(int i=0; i < GameParameters.UPGRADABLE_CRAFTING_NUMBER; i++)
            upgradableCrafting.add(null);
    }

    /**
     * Creates a new production instance.
     * Sets baseCrafting as specified and the others to empty.
     * @param baseCrafting sets the base crafting of the board.
     * @throws NullPointerException if baseCrafting is null
     */
    public Production(List<Crafting> baseCrafting) {
        if(baseCrafting == null)
            throw new NullPointerException();
        this.baseCrafting = baseCrafting;

        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>();

        for(int i=0; i < GameParameters.UPGRADABLE_CRAFTING_NUMBER; i++)
            upgradableCrafting.add(null);
    }

    /**
     * @return the list of base crafting
     */
    public List<Crafting> getAllBaseCrafting() {
        return new ArrayList<>(baseCrafting);
    }

    /**
     * @return the list of leader crafting
     */
    public List<Crafting> getAllLeaderCrafting() {
        return new ArrayList<>(leaderCrafting);
    }

    /**
     * @return the list of upgradable crafting
     */
    public List<UpgradableCrafting> getAllUpgradableCrafting() {
        return new ArrayList<>(upgradableCrafting);
    }

    /**
     * Adds to production a new leader crafting
     * @param crafting the crafting to be added
     * @throws NullPointerException if crafting is null
     */
    public void addLeaderCrafting(Crafting crafting) {
        if(crafting == null)
            throw new NullPointerException();

        leaderCrafting.add(crafting);
    }

    /**
     * Sets to production a new upgradable crafting in the specified slot
     * @param index the slot to be modified. Must be between 0 and GameParameters.UPGRADABLE_CRAFTING_NUMBER
     * @param crafting the new crafting to be set
     * @throws NullPointerException if crafting is null
     * @throws IndexOutOfBoundsException if index is out of bounds (negative or bigger than specified)
     */
    public void setUpgradableCrafting(int index, UpgradableCrafting crafting) {
        if(crafting == null)
            throw new NullPointerException();

        if(index < 0 || index > GameParameters.UPGRADABLE_CRAFTING_NUMBER - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (GameParameters.UPGRADABLE_CRAFTING_NUMBER - 1));

        upgradableCrafting.set(index, crafting);
    }
}
