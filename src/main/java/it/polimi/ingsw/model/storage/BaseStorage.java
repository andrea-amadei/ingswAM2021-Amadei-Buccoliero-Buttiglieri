package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransfer;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The BaseStorage class is a generic container of resources. It can contain a nearly unlimited amount of resources of
 * any kind.
 */
public class BaseStorage extends ResourceContainer {
    private final Map<ResourceSingle, Integer> resources;

    /**
     * Creates an empty base storage
     */
    public BaseStorage() {
        resources = new HashMap<>();
    }

    /**
     * Creates a storage containing the given resources
     * @param resources a Map containing the resources needed and their respective amount
     * @throws NullPointerException if resources is null
     * @throws IllegalArgumentException if any given amount is zero or below
     */
    public BaseStorage(Map<ResourceSingle, Integer> resources) {
        if(resources == null)
            throw new NullPointerException();

        for(ResourceSingle i : resources.keySet())
            if(resources.get(i) <= 0)
                throw new IllegalArgumentException("Resources value cannot be negative or zero");

        this.resources = resources;
    }

    /**
     * @return a map of the stored resources with their given amount
     */
    public Map<ResourceSingle, Integer> getAllResources() {
        return new HashMap<>(resources);
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
     * @throws IllegalResourceTransfer when trying to remove resources not available in the storage
     */
    public void removeResources(ResourceSingle resource, int amount) throws IllegalResourceTransfer {
        if(resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Resource amount must be above zero");

        if(!resources.containsKey(resource) || resources.get(resource) < amount)
            throw new IllegalResourceTransfer("Tried to remove a bigger amount of resources than stored");

        if(resources.get(resource) == amount)
            resources.remove(resource);
        else
            resources.put(resource, resources.get(resource) - amount);
    }

    /**
     * Sets a given amount of resources in the storage
     * @param resource the resource to set to the specific amount
     * @param amount the amount to set. Must be positive or zero
     * @throws NullPointerException if resource is null
     * @throws IllegalArgumentException if the amount is below zero
     */
    public void setResource(ResourceSingle resource, int amount) {
        if(resource == null)
            throw new NullPointerException();

        if(amount < 0)
            throw new IllegalArgumentException("Resource amount cannot be below zero");

        resources.put(resource, amount);
    }

    /**
     * Returns the amount of the given resource stored
     * @param resource the requested resource
     * @return the amount of the given resource stored. Returns 0 if none is currently stored or never was.
     * @throws NullPointerException is resource is null
     */
    public int getResource(ResourceSingle resource) {
        if(resource == null)
            throw new NullPointerException();

        if(!resources.containsKey(resource))
            return 0;

        return resources.get(resource);
    }

    /**
     * Clears all resources and resets their tracking. Good as new!
     */
    public void reset() {
        resources.clear();
    }

    @Override
    public boolean equals(Object o) {
        // if they are the same return true
        if (this == o)
            return true;

        // if one is null or they are different objects return false
        if (o == null || getClass() != o.getClass())
            return false;

        BaseStorage that = (BaseStorage) o;

        // if the size is different returns false
        if(this.getAllResources().size() != that.getAllResources().size())
            return false;

        for(ResourceSingle i : this.resources.keySet()) {
            // if the key exists but the amount is different returns false
            if (that.resources.containsKey(i)) {
                if (this.resources.get(i).intValue() != that.resources.get(i).intValue())
                    return false;

            // if the key doesn't exists return false
            } else {
                return false;
            }
        }

        // finally returns true
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources);
    }
}
