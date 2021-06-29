package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The BaseStorage class is a generic container of resources. It can contain a nearly unlimited amount of resources of
 * any kind.
 */
public class BaseStorage extends ResourceContainer {
    private final Map<ResourceSingle, Integer> resources;
    private final String id;

    /**
     * Creates an empty base storage
     */
    public BaseStorage(String id) {
        if(id == null)
            throw new NullPointerException();

        resources = new HashMap<>();
        this.id = id;
    }

    /**
     * Creates a storage containing the given resources
     * @param resources a Map containing the resources needed and their respective amount
     * @throws NullPointerException if resources is null
     * @throws IllegalArgumentException if any given amount is zero or below
     */
    public BaseStorage(Map<ResourceSingle, Integer> resources, String id) {
        if(resources == null || id == null)
            throw new NullPointerException();

        for(ResourceSingle i : resources.keySet())
            if(resources.get(i) <= 0)
                throw new IllegalArgumentException("Resources value cannot be negative or zero");

        this.resources = resources;
        this.id = id;
    }

    /**
     * Returns a map of the stored resources with their amount
     * @return a map of the stored resources with their amount
     */
    public Map<ResourceSingle, Integer> getAllResources() {
        return new HashMap<>(resources);
    }

    /**
     * Returns the id of this storage
     * @return the id of this storage
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Adds a given amount of resources to the storage
     * @param resource the resource to add to the storage
     * @param amount the amount of resource to add. Must be positive
     * @throws NullPointerException if resource is null
     * @throws IllegalArgumentException if the amount is zero or below
     */
    public void addResources(ResourceSingle resource, int amount) {
        if(resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Resource amount must be above zero");

        if(resources.containsKey(resource))
            resources.put(resource, resources.get(resource) + amount);
        else
            resources.put(resource, amount);
    }

    /**
     * Removes a given amount of resources from the storage
     * @param resource the resource to remove from the storage
     * @param amount the amount of resource to remove. Must be positive
     * @throws NullPointerException if resource is null
     * @throws IllegalArgumentException if the amount is zero or below
     * @throws IllegalResourceTransferException when trying to remove resources not available in the storage
     */
    public void removeResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException {
        if(resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Resource amount must be above zero");

        if(!resources.containsKey(resource) || resources.get(resource) < amount)
            throw new IllegalResourceTransferException("Tried to remove a bigger amount of resources than stored");

        if(resources.get(resource) == amount)
            resources.remove(resource);
        else
            resources.put(resource, resources.get(resource) - amount);
    }

    /**
     * Returns the amount of the given resource stored
     * @param resource the requested resource
     * @return the amount of the given resource stored. Returns 0 if none is currently stored or never was.
     * @throws NullPointerException if resource is null
     */
    public int getResources(ResourceSingle resource) {
        if(resource == null)
            throw new NullPointerException();

        if(!resources.containsKey(resource))
            return 0;

        return resources.get(resource);
    }

    /**
     * Returns the total amount of resources (of any kind) stored
     * @return the total amount of resources stored
     */
    public int  totalAmountOfResources() {
        int tot = 0;

        for(ResourceSingle i : resources.keySet())
            tot += resources.get(i);

        return tot;
    }

    /**
     * Clears all resources and resets their tracking. Good as new!
     */
    public void reset() {
        resources.clear();
    }

    @Override
    public RawStorage toRaw() {
        return new RawStorage(this);
    }

    @Override
    public void printDebugInfo() {
        Logger.log(toString());
    }

}
