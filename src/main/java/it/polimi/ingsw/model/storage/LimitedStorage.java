package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The LimitedStorage class is a generic container of resources with custom limits.
 * Limits are specified during the object construction and will stand for its entire lifetime. While adding or
 * removing resources, an IllegalResourceTransferException can be thrown if the operation cannot be completed without
 * exceeding the storage limits.
 */
public class LimitedStorage extends BaseStorage {
    private final Map<ResourceSingle, Integer> singleResourceLimit;
    private final Map<ResourceGroup, Integer> groupResourceLimit;

    private final BaseStorage other = new BaseStorage();
    private transient ResourceGroup group;

    /**
     * Creates an empty Limited Storage with the given limits to it
     * @param singleResourceLimit a Map containing a whitelist of resources admitted with their maximum amount.
     *                            Cannot be null. Must contain at least one resource. Amount limits must be above zero
     * @param groupResourceLimit a Map containing a whitelist of resource groups admitted with their maximum amount.
     *                           Can be null or empty. Amount limits must be above zero.
     *                           <b>Currently only one group at a time is allowed!</b>
     * @throws NullPointerException if singleResourceLimit is null
     * @throws UnsupportedOperationException if the groupResourceLimit contains more than one limit.
     *                                       Might be lifted in the future!
     * @throws IllegalArgumentException if any of the limits specify an amount equal or below zero
     */
    public LimitedStorage(Map<ResourceSingle, Integer> singleResourceLimit, Map<ResourceGroup, Integer> groupResourceLimit) {
        super();

        if(singleResourceLimit == null)
            throw new NullPointerException();

        if(groupResourceLimit == null)
            groupResourceLimit = new HashMap<>();

        //TODO: to be removed in future
        if(groupResourceLimit.size() > 1)
            throw new UnsupportedOperationException("Only one group limit is currently expected");

        for(ResourceSingle i : singleResourceLimit.keySet())
            if(singleResourceLimit.get(i) <= 0)
                throw new IllegalArgumentException("Limit values cannot be negative or zero");

        for(ResourceGroup i : groupResourceLimit.keySet())
            if(groupResourceLimit.get(i) <= 0)
                throw new IllegalArgumentException("Limit values cannot be negative or zero");

        this.singleResourceLimit = singleResourceLimit;
        this.groupResourceLimit = groupResourceLimit;

        if(groupResourceLimit.size() == 1)
            group = groupResourceLimit.keySet().iterator().next();
    }

    /**
     * Creates a Limited Storage containing the given resources and with the given limits in place
     * @param resources a Map containing the resources needed and their respective amount
     * @param singleResourceLimit a Map containing a whitelist of resources admitted with their maximum amount.
     *                            Cannot be null. Must contain at least one resource. Amount limits must be above zero
     * @param groupResourceLimit a Map containing a whitelist of resource groups admitted with their maximum amount.
     *                           Can be null or empty. Amount limits must be above zero.
     *                           <b>Currently only one group at a time is allowed!</b>
     * @throws NullPointerException if resources or singleResourceLimit are null
     * @throws UnsupportedOperationException if the groupResourceLimit contains more than one limit.
     *                                       Might be lifted in the future!
     * @throws IllegalArgumentException if any of the limits specify an amount equal or below zero or the given initial
     *                                  resources cannot fit in their own limits
     */
    public LimitedStorage(Map<ResourceSingle, Integer> resources, Map<ResourceSingle, Integer> singleResourceLimit, Map<ResourceGroup, Integer> groupResourceLimit) {
        super();

        if(resources == null || singleResourceLimit == null)
            throw new NullPointerException();

        if(groupResourceLimit == null)
            groupResourceLimit = new HashMap<>();

        //TODO: to be removed in future
        if(groupResourceLimit.size() > 1)
            throw new UnsupportedOperationException("Only one group limit is currently expected");

        for(ResourceSingle i : singleResourceLimit.keySet())
            if(singleResourceLimit.get(i) <= 0)
                throw new IllegalArgumentException("Limit values cannot be negative or zero");

        for(ResourceGroup i : groupResourceLimit.keySet())
            if(groupResourceLimit.get(i) <= 0)
                throw new IllegalArgumentException("Limit values cannot be negative or zero");

        this.singleResourceLimit = singleResourceLimit;
        this.groupResourceLimit = groupResourceLimit;

        if(groupResourceLimit.size() == 1)
            group = groupResourceLimit.keySet().iterator().next();

        for(ResourceSingle i : resources.keySet())
            try {
                addResources(i, resources.get(i));
            } catch (IllegalResourceTransferException e) {
                throw new IllegalArgumentException("Unable to create object with resources exceeding limits");
            }
    }

    /**
     * @return a map of the stored resources with their amount
     */
    public Map<ResourceSingle, Integer> getAllResources() {
        Map<ResourceSingle, Integer> map = new HashMap<>(super.getAllResources());

        for(ResourceSingle i : other.getAllResources().keySet())
            if(map.containsKey(i))
                map.put(i, map.get(i) + other.getResources(i));
            else
                map.put(i, other.getResources(i));

        return map;
    }

    /**
     * Adds a given amount of resources to the storage
     * @param resource the resource to add to the storage
     * @param amount the amount of resource to add. Must be positive
     * @throws NullPointerException if resource is null
     * @throws IllegalArgumentException if the amount is zero or below
     * @throws IllegalResourceTransferException when trying to add resources exceeding the storage limits
     */
    public void addResources(ResourceSingle resource, int amount) throws IllegalResourceTransferException {
        if(resource == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Resource amount must be above zero");

        int leftToAssign = amount;
        int toStorage = 0;

        // if the resource is admitted
        if(singleResourceLimit.containsKey(resource)) {

            // if the resource doesn't exceed the limits assign all of it. We are good!
            if(amount + super.getResources(resource) <= singleResourceLimit.get(resource)) {
                super.addResources(resource, amount);
                return;
            }

            // else, if the resource exceed the limits, assign ony what can fit and keep the reset for later
            else {
                // compute the maximum assignable amount
                toStorage = singleResourceLimit.get(resource) - super.getResources(resource);

                // compute what's left
                leftToAssign -= toStorage;
            }
        }

        // if there is no other storage or cannot fit the group criteria, we cannot assign all of it :(
        if(groupResourceLimit.size() == 0 || !resource.isA(group))
            throw new IllegalResourceTransferException("The requested amount to add could not fit within the limits");

        // if what's left to assign can't fit the other storage, we cannot assign all of it :(  (again)
        if(leftToAssign + other.totalAmountOfResources() > groupResourceLimit.get(group))
            throw new IllegalResourceTransferException("The requested amount to add could not fit within the limits");

        // finally, we can assign all of it :)
        if(toStorage > 0)
            super.addResources(resource, toStorage);

        other.addResources(resource, leftToAssign);
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

        int leftToRemove = amount;
        int fromOther = 0;

        // if the other storage contains some resource
        if(other.getResources(resource) > 0) {

            // if the resource is entirely in the other storage, just remove it. We are all good :)
            if(other.getResources(resource) >= amount) {
                other.removeResources(resource, amount);
                return;
            }

            // else, if the resource is split between the two storages
            else {
                // compute the maximum amount to remove from the other storage
                fromOther = other.getResources(resource);

                // computed what's left to remove
                leftToRemove -= fromOther;
            }
        }

        // if there is not enough to remove from the regular storage, we cannot remove it :(
        if(super.getResources(resource) < leftToRemove)
            throw new IllegalResourceTransferException("The requested amount to remove was not available in the storage");

        // finally, we can remove all of it :)
        if(fromOther > 0)
            other.removeResources(resource, fromOther);

        super.removeResources(resource, leftToRemove);
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

        return super.getResources(resource) + other.getResources(resource);
    }

    /**
     * Returns the total amount of resources (of any kind) stored
     * @return the total amount of resources stored
     */
    public int  totalAmountOfResources() {
        int tot = 0;

        for(ResourceSingle i : super.getAllResources().keySet())
            tot += super.getResources(i);

        for(ResourceSingle i : other.getAllResources().keySet())
            tot += other.getResources(i);

        return tot;
    }

    /**
     * Clears all resources and resets their tracking. All the limits stand in place. Good as new!
     */
    public void reset() {
        super.reset();
        other.reset();
    }

    /**
     * @return a map of the single resource's limits in place
     */
    public Map<ResourceSingle, Integer> getSingleResourceLimit() {
        return new HashMap<>(singleResourceLimit);
    }

    /**
     * @return a map of the group resource's limits in place
     */
    public Map<ResourceGroup, Integer> getGroupResourceLimit() {
        return new HashMap<>(groupResourceLimit);
    }

    @Override
    public boolean equals(Object o) {
        // if they are the same return true
        if (this == o)
            return true;

        // if one is null or they are different objects return false
        if (o == null || getClass() != o.getClass())
            return false;

        LimitedStorage that = (LimitedStorage) o;

        // CHECK SINGLE RESOURCE LIMITS
        // if the size is different returns false
        if(this.getSingleResourceLimit().size() != that.getSingleResourceLimit().size())
            return false;

        for(ResourceSingle i : this.getSingleResourceLimit().keySet()) {
            // if the key exists but the amount is different returns false
            if (that.getSingleResourceLimit().containsKey(i)) {
                if (this.getSingleResourceLimit().get(i).intValue() != that.getSingleResourceLimit().get(i).intValue())
                    return false;

            // if the key doesn't exists return false
            } else {
                return false;
            }
        }

        // CHECK GROUP RESOURCE LIMITS
        // if the size is different returns false
        if(this.getGroupResourceLimit().size() != that.getGroupResourceLimit().size())
            return false;

        for(ResourceGroup i : this.getGroupResourceLimit().keySet()) {
            // if the key exists but the amount is different returns false
            if (that.getGroupResourceLimit().containsKey(i)) {
                if (this.getGroupResourceLimit().get(i).intValue() != that.getGroupResourceLimit().get(i).intValue())
                    return false;

            // if the key doesn't exists return false
            } else {
                return false;
            }
        }

        // CHECK IF STORED RESOURCES ARE THE SAME
        // if the size is different returns false
        if(this.getAllResources().size() != that.getAllResources().size())
            return false;

        for(ResourceSingle i : this.getAllResources().keySet()) {
            // if the key exists but the amount is different returns false
            if (that.getAllResources().containsKey(i)) {
                if (this.getAllResources().get(i).intValue() != that.getAllResources().get(i).intValue())
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
        return Objects.hash(super.hashCode(), singleResourceLimit, groupResourceLimit, other);
    }
}
