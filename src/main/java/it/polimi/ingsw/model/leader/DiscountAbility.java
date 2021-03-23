package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.model.Player;

/**
 * Class DiscountAbility implements SpecialAbility Interface
 * represents leader card ability to get discounts on a set resource type when buying
 */
public class DiscountAbility implements SpecialAbility {

    private final int amount;
    private final ResourceType resource;

    /**
     * DiscountAbility Constructor
     * @param amount the amount that is going to be discounted
     * @param resource the type of resource that can be bought for discounted price
     * @throws NullPointerException if resource pointer is null
     * @throws IllegalArgumentException if null discount amount
     * Discount amount can be negative, to add customization to the game, but it cannot be zero
     */
    public DiscountAbility(int amount, ResourceType resource){

        if(amount == 0)
            throw new IllegalArgumentException("Discount amount cannot be zero");

        if(resource == null)
            throw new NullPointerException();

        this.amount = amount;
        this.resource = resource;

    }

    /**
     * get amount of discount
     * @return the amount of discount available to the player
     */
    public int getAmount(){
        return amount;
    }

    /**
     * get resource type
     * @return the type of resource that can be bought for discounted price
     */
    public ResourceType getResource() {
        return resource;
    }


    @Override
    public void activate(Player player) {

        //TODO: activate function
    }
}
