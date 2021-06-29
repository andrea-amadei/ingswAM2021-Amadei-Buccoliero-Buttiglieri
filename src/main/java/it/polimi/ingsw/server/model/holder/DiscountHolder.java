package it.polimi.ingsw.server.model.holder;

import it.polimi.ingsw.server.model.basetypes.ResourceSingle;

import java.util.HashMap;
import java.util.Map;

/**
 * The DiscountHolder class contains all the discounts available to a player
 */
public class DiscountHolder {
    private final Map<ResourceSingle, Integer> discounts;

    /**
     * Creates the Discount Holder
     */
    public DiscountHolder() {
        discounts = new HashMap<>();
    }

    /**
     * @return a Map of all the discounted resources and by which amount
     */
    public Map<ResourceSingle, Integer> getDiscounts() {
        return new HashMap<>(discounts);
    }

    /**
     * Adds a new discount or updates an old one. Discounts on the same resource are cumulated
     * @param resource the resource to add the discount to
     * @param discount the amount of the discount. Can also accept negative discounts (overprices). Cannot be zero
     * @return true if an old discount was replaced, false if a new one was created
     * @throws NullPointerException if resource is null
     * @throws IllegalArgumentException if discount is 0
     */
    public boolean addDiscount(ResourceSingle resource, int discount) {
        if(resource == null)
            throw new NullPointerException();

        if(discount == 0)
            throw new IllegalArgumentException("Discount cannot be zero");

        boolean replaced = discounts.containsKey(resource);

        if(replaced)
            discounts.put(resource, discounts.get(resource) + discount);
        else
            discounts.put(resource, discount);

        return replaced;
    }

    /**
     * Gets the total discount on a given resource
     * @param resource the resource to check for discounts
     * @return the total discount associated with the given resource. Returns 0 if there are none
     * @throws NullPointerException if resource is null
     */
    public int totalDiscountByResource(ResourceSingle resource) {
        if(resource == null)
            throw new NullPointerException();

        if(!discounts.containsKey(resource))
            return 0;

        return discounts.get(resource);
    }

    /**
     * Clears all discounts
     */
    public void reset() {
        discounts.clear();
    }
}
