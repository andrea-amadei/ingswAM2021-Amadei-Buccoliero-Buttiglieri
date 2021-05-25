package it.polimi.ingsw.model.production;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.NotReadyToCraftException;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The production class defines the methods to interact with every crafting aspect of the game.
 * The class holds 3 lists of different types of crafting:
 *      - Base crafting are granted to everyone at the start of the game
 *      - Leader crafting are added by the some of the leaders' abilities
 *      - Upgradable crafting can be purchased or upgraded from the shop
 */
public class Production {

    public enum CraftingType {
        BASE,
        UPGRADABLE,
        LEADER
    }

    private final List<Crafting> baseCrafting;
    private final List<Crafting> leaderCrafting;
    private final List<UpgradableCrafting> upgradableCrafting;

    private Integer selectedIndex;
    private CraftingType selectedType;

    /**
     * Creates a new production instance.
     * Set every crafting slot to empty.
     * This constructor should only be used in testing. The builder will use the other one
     */
    public Production() {
        baseCrafting = new ArrayList<>();
        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>(GameParameters.UPGRADABLE_CRAFTING_NUMBER);

        for(int i=0; i < GameParameters.UPGRADABLE_CRAFTING_NUMBER; i++)
            upgradableCrafting.add(null);

        baseCrafting.add(new Crafting(new HashMap<>(){{
            put(ResourceTypeSingleton.getInstance().getAnyResource(), 2);
        }}, new HashMap<>(){{
            put(ResourceTypeSingleton.getInstance().getAnyResource(), 1);
        }}, 0));
        selectedIndex = null;
        selectedType = null;
    }

    /**
     * Creates a new production instance.
     * Set every crafting slot to empty.
     * This constructor is used by the builder
     *
     * @param upgradableCraftingNumber the number of upgradableCrafting slots
     */
    public Production(int upgradableCraftingNumber){
        baseCrafting = new ArrayList<>();
        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>(upgradableCraftingNumber);

        for(int i=0; i < upgradableCraftingNumber; i++)
            upgradableCrafting.add(null);

        selectedIndex = null;
        selectedType = null;
    }

    /**
     * Creates a new production instance.
     * Sets baseCrafting as specified and the others to empty.
     * @param baseCrafting sets the base crafting of the board.
     * @throws NullPointerException if baseCrafting is null
     */
    public Production(int upgradableCraftingNumber, List<Crafting> baseCrafting) {
        if(baseCrafting == null)
            throw new NullPointerException();
        this.baseCrafting = baseCrafting;

        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>();

        for(int i=0; i < upgradableCraftingNumber; i++)
            upgradableCrafting.add(null);

        selectedIndex = null;
        selectedType = null;
    }

    /**
     * @return the list of base crafting
     */
    public List<Crafting> getAllBaseCrafting() {
        return new ArrayList<>(baseCrafting);
    }

    /**
     * Returns the specified base crafting
     * @param index the index of the selected base crafting
     * @return the specified base crafting
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Crafting getBaseCrafting(int index) {
        return baseCrafting.get(index);
    }

    /**
     * @return the list of leader crafting
     */
    public List<Crafting> getAllLeaderCrafting() {
        return new ArrayList<>(leaderCrafting);
    }

    /**
     * Returns the specified leader crafting
     * @param index the index of the selected leader crafting
     * @return the specified leader crafting
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Crafting getLeaderCrafting(int index) {
        return leaderCrafting.get(index);
    }

    /**
     * @return the list of upgradable crafting
     */
    public List<UpgradableCrafting> getAllUpgradableCrafting() {
        return new ArrayList<>(upgradableCrafting);
    }

    /**
     * Returns the specified upgradable crafting
     * @param index the index of the selected upgradable crafting
     * @return the specified upgradable crafting
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public UpgradableCrafting getUpgradableCrafting(int index) {
        return upgradableCrafting.get(index);
    }

    /**
     * Returns a crafting, given its type (BASE, UPGRADABLE or LEADER) and its index.
     * @param type the type of crafting. Can either be BASE, UPGRADABLE or LEADER
     * @param index the index of the crafting, starting from 0. Must be inbounds
     * @return the requested crafting
     * @throws NullPointerException if type is null
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Crafting getCrafting(CraftingType type, int index) {
        if(type == null)
            throw new NullPointerException();

        switch (type) {
            case UPGRADABLE:
                return getUpgradableCrafting(index);

            case BASE:
                return getBaseCrafting(index);

            case LEADER:
                return getLeaderCrafting(index);

            // This shouldn't be happening at all
            default:
                return null;
        }
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

        if(index < 0 || index >= upgradableCrafting.size())
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (upgradableCrafting.size() - 1));

        upgradableCrafting.set(index, crafting);
    }

    /**
     * Mark as selected a crafting from the production.
     * @param type the crafting type (BASE, UPGRADABLE or LEADER)
     * @param index the index of the crafting
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void selectCrafting(CraftingType type, int index){
        if(type == null)
            throw new NullPointerException();

        if(index < 0 || index >= upgradableCrafting.size())
            throw new IndexOutOfBoundsException("The index specified for the upgradable crafting is out of bound");

        selectedIndex = index;
        selectedType = type;
    }

    /**
     * Returns the selected crafting index.
     * @return the selected crafting index.
     */
    public Integer getSelectedCraftingIndex() {
        if(selectedIndex == null)
            throw new NoSuchElementException("No element was selected");

        return selectedIndex;
    }

    /**
     * Returns the selected crafting type.
     * @return the selected crafting type.
     */
    public CraftingType getSelectedCraftingType() {
        if(selectedType == null)
            throw new NoSuchElementException("No element was selected");

        return selectedType;
    }

    /**
     * Returns the selected crafting. Throws NoSuchElementException if none is selected.
     * @return the selected crafting
     * @throws NoSuchElementException if nothing is currently selected
     */
    public Crafting getSelectedCrafting() {
        if(selectedType == null || selectedIndex == null)
            throw new NoSuchElementException("No element was selected");

        return getCrafting(selectedType, selectedIndex);
    }

    /**
     * Returns true if a crating is selected, false otherwise
     * @return true if a crating is selected, false otherwise
     */
    public boolean isCraftingSelected() {
        return selectedIndex != null && selectedType != null;
    }

    /**
     * Returns true if at least a crafting is ready, false otherwise
     * @return true if at least a crafting is ready, false otherwise
     */
    public boolean isCraftingReady() {
        boolean ready = false;

        for(Crafting i : baseCrafting)
            if(i != null && i.readyToCraft()) {
                ready = true;
            }

        for(Crafting i : upgradableCrafting)
            if(i != null && i.readyToCraft()) {
                ready = true;
            }

        for(Crafting i : leaderCrafting)
            if(i != null && i.readyToCraft()) {
                ready = true;
            }

        return ready;
    }

    /**
     * Removes the selection from the previously selected upgradable crafting
     */
    public void resetCraftingSelection(){
        selectedIndex = null;
        selectedType = null;
    }

    /**
     * Activates all productions that are ready
     * @param player the player activating the production
     * @param fp the faith path
     * @throws NullPointerException if either player or fp is null
     * @throws NotReadyToCraftException if no crafting is ready to craft
     */
    public List<PayloadComponent> activateProduction(Player player, FaithPath fp) {
        if(player == null || fp == null)
            throw new NullPointerException();

        if(!isCraftingReady())
            throw new NotReadyToCraftException("No crafting is ready to craft");

        List<PayloadComponent> payload = new ArrayList<>();
        for(Crafting i : baseCrafting)
            if(i != null && i.readyToCraft()) {
                payload.addAll(i.activateCrafting(player, fp));
                payload.add(PayloadFactory.changeCraftingStatus(player.getUsername(), false, baseCrafting.indexOf(i), CraftingType.BASE));
            }

        for(UpgradableCrafting i : upgradableCrafting)
            if(i!= null && i.readyToCraft()) {
                payload.addAll(i.activateCrafting(player, fp));
                payload.add(PayloadFactory.changeCraftingStatus(player.getUsername(), false, upgradableCrafting.indexOf(i), CraftingType.UPGRADABLE));
            }

        for(Crafting i : leaderCrafting)
            if(i != null && i.readyToCraft()) {
                payload.addAll(i.activateCrafting(player, fp));
                payload.add(PayloadFactory.changeCraftingStatus(player.getUsername(), false, leaderCrafting.indexOf(i), CraftingType.LEADER));
            }

        return payload;
    }
}
