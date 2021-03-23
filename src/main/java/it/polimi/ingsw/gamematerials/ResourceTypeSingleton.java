package it.polimi.ingsw.gamematerials;

import java.util.Arrays;
import java.util.HashSet;

public class ResourceTypeSingleton {
    private static final ResourceTypeSingleton instance = new ResourceTypeSingleton();

    final ResourceSingle GOLD = new ResourceSingle("Gold");
    final ResourceSingle SERVANT = new ResourceSingle("Servant");
    final ResourceSingle SHIELD = new ResourceSingle("Shield");
    final ResourceSingle STONE = new ResourceSingle("Stone");
    final ResourceGroup ANY = new ResourceGroup("Any", new HashSet<>(Arrays.asList(GOLD, SERVANT, SHIELD, STONE)));


    private ResourceTypeSingleton() {
    }

    public static ResourceTypeSingleton getInstance(){
        return instance;
    }

    public ResourceSingle getGoldResource() {return GOLD;}
    public ResourceSingle getServantResource() {return SERVANT;}
    public ResourceSingle getShieldResource() {return SHIELD;}
    public ResourceSingle getStoneResource() {return STONE;}

    public ResourceGroup getAnyResource() {return ANY;}
}
