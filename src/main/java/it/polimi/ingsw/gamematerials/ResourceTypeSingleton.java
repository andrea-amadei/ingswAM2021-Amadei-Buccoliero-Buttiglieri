package it.polimi.ingsw.gamematerials;

import java.util.Arrays;
import java.util.HashSet;

/**
 * The ResourceTypeSingleton class implements a singleton design pattern.
 * It can be used to get predefined instances of resources.
 */
public class ResourceTypeSingleton {
    private static final ResourceTypeSingleton instance = new ResourceTypeSingleton();

    final ResourceSingle GOLD = new ResourceSingle("Gold");
    final ResourceSingle SERVANT = new ResourceSingle("Servant");
    final ResourceSingle SHIELD = new ResourceSingle("Shield");
    final ResourceSingle STONE = new ResourceSingle("Stone");
    final ResourceGroup ANY = new ResourceGroup("Any", new HashSet<>(Arrays.asList(GOLD, SERVANT, SHIELD, STONE)));

    private ResourceTypeSingleton() {}

    /**
     * Can be sued to obtain access to resources
     * @return a initiated version of himself
     */
    public static ResourceTypeSingleton getInstance(){
        return instance;
    }

    /**
     * @return gold resource
     */
    public ResourceSingle getGoldResource() {return GOLD;}

    /**
     * @return servant resource
     */
    public ResourceSingle getServantResource() {return SERVANT;}

    /**
     * @return shield resource
     */
    public ResourceSingle getShieldResource() {return SHIELD;}

    /**
     * @return stone resource
     */
    public ResourceSingle getStoneResource() {return STONE;}

    /**
     * @return the generic resource any, composed by gold, servant, shield and stone
     */
    public ResourceGroup getAnyResource() {return ANY;}
}
