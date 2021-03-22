package it.polimi.ingsw.model.production;

import it.polimi.ingsw.model.GameParameters;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private final List<Crafting> baseCrafting;
    private final List<Crafting> leaderCrafting;
    private final List<UpgradableCrafting> upgradableCrafting;

    public Production() {
        baseCrafting = new ArrayList<>();
        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>(GameParameters.UPGRADABLE_CRAFTING_NUMBER);

        for(int i=0; i < GameParameters.UPGRADABLE_CRAFTING_NUMBER; i++)
            upgradableCrafting.add(null);
    }

    public Production(List<Crafting> baseCrafting) {
        if(baseCrafting == null)
            throw new NullPointerException();
        this.baseCrafting = baseCrafting;

        leaderCrafting = new ArrayList<>();
        upgradableCrafting = new ArrayList<>();

        for(int i=0; i < GameParameters.UPGRADABLE_CRAFTING_NUMBER; i++)
            upgradableCrafting.add(null);
    }

    public List<Crafting> getBaseCrafting() {
        return new ArrayList<>(baseCrafting);
    }

    public List<Crafting> getLeaderCrafting() {
        return new ArrayList<>(leaderCrafting);
    }

    public List<UpgradableCrafting> getUpgradableCrafting() {
        return new ArrayList<>(upgradableCrafting);
    }

    public void addLeaderCrafting(Crafting crafting) {
        if(crafting == null)
            throw new NullPointerException();

        leaderCrafting.add(crafting);
    }

    public void setUpgradableCrafting(int index, UpgradableCrafting crafting) {
        if(crafting == null)
            throw new NullPointerException();

        if(index < 0 || index > GameParameters.UPGRADABLE_CRAFTING_NUMBER - 1)
            throw new IllegalArgumentException("Index out of bounds");

        upgradableCrafting.set(index, crafting);
    }
}
