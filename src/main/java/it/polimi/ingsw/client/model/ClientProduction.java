package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;

import java.util.ArrayList;
import java.util.List;

public class ClientProduction implements Observable<ClientProduction> {

    private final List<Listener<ClientProduction>> listeners;
    private final int upgradableCraftingNumber;

    private final List<RawCrafting> baseCraftings;
    private final List<Boolean> baseCraftingsReady;

    private final List<RawCrafting> upgradableCraftings;
    private final List<Integer> upgradableLevels;
    private final List<Boolean> upgradableCraftingsReady;

    private final List<RawCrafting> leaderCraftings;
    private final List<Boolean> leaderCraftingsReady;

    private Integer selectedCraftingIndex;
    private Production.CraftingType selectedType;


    /**
     * Creates the production. The base craftings are an empty list.
     * The specified amount of slot are represented as null in the upgradableCrafting list.
     * @param upgradableCraftingNumber the number of upgradable craftings
     */
    public ClientProduction(int upgradableCraftingNumber){

        this.upgradableCraftingNumber = upgradableCraftingNumber;

        this.baseCraftings = new ArrayList<>();
        this.baseCraftingsReady = new ArrayList<>();

        this.upgradableCraftings = new ArrayList<>();
        this.upgradableLevels = new ArrayList<>();
        this.upgradableCraftingsReady = new ArrayList<>();

        this.leaderCraftings = new ArrayList<>();
        this.leaderCraftingsReady = new ArrayList<>();

        for(int i = 0; i < upgradableCraftingNumber; i++) {
            upgradableCraftings.add(null);
            upgradableLevels.add(0);
            upgradableCraftingsReady.add(false);
        }

        this.selectedCraftingIndex = null;
        this.selectedType = null;

        this.listeners = new ArrayList<>();
    }

    /**
     * Adds a base crafting. It should be added from the builder once it receives the
     * config file
     * @param baseCrafting the base crafting
     */
    public synchronized void addBaseCrafting(RawCrafting baseCrafting){
        this.baseCraftings.add(baseCrafting);
        baseCraftingsReady.add(false);
        update();
    }

    /**
     * Sets an upgradable crafting int the list of upgradable craftings at the correct position.
     * It also update the level in the parallel list
     * @param crafting the crafting to be put in the production
     * @param level the level of the crafting
     * @param index the index of the slot where the crafting is put
     */
    public synchronized void addUpgradableCrafting(RawCrafting crafting, int level, int index){
        upgradableCraftings.set(index, crafting);
        upgradableLevels.set(index, level);
        upgradableCraftingsReady.set(index, false);
        update();
    }

    /**
     * Adds a new crafting to the leader crafting list
     * @param crafting the crafting to be added
     */
    public synchronized void addLeaderCrafting(RawCrafting crafting){
        leaderCraftings.add(crafting);
        leaderCraftingsReady.add(false);
        update();
    }

    public synchronized void setCraftingStatus(Production.CraftingType craftingType, int index, boolean status){
        switch(craftingType){
            case UPGRADABLE:
                upgradableCraftingsReady.set(index, status);
                break;
            case BASE:
                baseCraftingsReady.set(index, status);
                break;
            case LEADER:
                leaderCraftingsReady.set(index, status);
                break;
        }
        update();
    }

    /**
     * Selects a crafting by specifying the type and the index
     * @param selectedType the type of crafting selected
     * @param index the index of the selected crafting
     */
    public synchronized void selectCrafting(Production.CraftingType selectedType, int index){
        this.selectedType = selectedType;
        this.selectedCraftingIndex = index;
        update();
    }

    /**
     * Clears the selection
     */
    public synchronized void unselect(){
        selectedCraftingIndex = null;
        selectedType = null;
        update();
    }

    public synchronized int getUpgradableCraftingNumber() {
        return upgradableCraftingNumber;
    }

    public synchronized List<RawCrafting> getBaseCraftings() {
        return new ArrayList<>(baseCraftings);
    }

    public synchronized List<RawCrafting> getUpgradableCraftings() {
        return new ArrayList<>(upgradableCraftings);
    }

    public synchronized List<Integer> getUpgradableLevels() {
        return new ArrayList<>(upgradableLevels);
    }

    public synchronized List<RawCrafting> getLeaderCraftings() {
        return new ArrayList<>(leaderCraftings);
    }

    public synchronized Integer getSelectedCraftingIndex() {
        return selectedCraftingIndex;
    }

    public synchronized Production.CraftingType getSelectedType() {
        return selectedType;
    }

    public synchronized List<Boolean> getBaseCraftingsReady() {
        return new ArrayList<>(baseCraftingsReady);
    }

    public synchronized List<Boolean> getUpgradableCraftingsReady() {
        return new ArrayList<>(upgradableCraftingsReady);
    }

    public synchronized List<Boolean> getLeaderCraftingsReady() {
        return new ArrayList<>(leaderCraftingsReady);
    }

    @Override
    public synchronized void addListener(Listener<ClientProduction> listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void update() {
        for(Listener<ClientProduction> l : listeners)
            l.update(this);
    }
}
