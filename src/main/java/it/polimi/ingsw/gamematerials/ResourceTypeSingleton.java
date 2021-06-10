package it.polimi.ingsw.gamematerials;

import java.util.*;

/**
 * The ResourceTypeSingleton class implements a singleton design pattern.
 * It can be used to get predefined instances of resources.
 */
public class ResourceTypeSingleton {
    private static final ResourceTypeSingleton instance = new ResourceTypeSingleton();

    final ResourceSingle GOLD = new ResourceSingle("gold");
    final ResourceSingle SERVANT = new ResourceSingle("servant");
    final ResourceSingle SHIELD = new ResourceSingle("shield");
    final ResourceSingle STONE = new ResourceSingle("stone");
    final ResourceGroup ANY = new ResourceGroup("any", new HashSet<>(Arrays.asList(GOLD, SERVANT, SHIELD, STONE)));

    private ResourceTypeSingleton() {}

    /**
     * Can be used to obtain access to resources
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

    /**
     * Returns the matching resource
     * @param name the name of the single resource
     * @return the resource single matching the given name
     */
    public ResourceSingle getResourceSingleByName(String name) {
        if(name == null)
            throw new NullPointerException();

        name = name.toLowerCase();

        switch (name) {
            case "gold":
                return GOLD;
            case "servant":
                return SERVANT;
            case "shield":
                return SHIELD;
            case "stone":
                return STONE;

            default:
                throw new NoSuchElementException("No such resource as \"" + name + "\"");
        }
    }

    /**
     * Returns the matching resource
     * @param name the name of the group resource
     * @return the resource group matching the given name
     */
    public ResourceGroup getResourceGroupByName(String name) {
        if(name == null)
            throw new NullPointerException();

        name = name.toLowerCase();

        if(name.equals("any"))
            return ANY;

        throw new NoSuchElementException("No such resource as \"" + name + "\"");
    }

    /**
     * Returns the matching resource
     * @param name the name of the resource
     * @return the resource matching the given name
     */
    public ResourceType getResourceTypeByName(String name) {
        try {
            return getResourceSingleByName(name);
        } catch (NoSuchElementException e1) {
            return getResourceGroupByName(name);
        }
    }

    public ResourceSingle getRandomResourceSingle(){
        Random random = new Random();
        int n = random.nextInt(4);
        switch (n) {
            case 0: return GOLD;
            case 1: return SHIELD;
            case 2: return STONE;
            default: return SERVANT;
        }
    }
}
