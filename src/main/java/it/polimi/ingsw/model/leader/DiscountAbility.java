package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;

/**
 * Class DiscountAbility implements SpecialAbility Interface
 * represents leader card ability to get discounts on a set resource type when buying
 */
public class DiscountAbility implements SpecialAbility {

    private final int amount;
    private final ResourceSingle resource;

    /**
     * DiscountAbility Constructor
     * @param amount the amount that is going to be discounted
     * @param resource the type of resource that can be bought for discounted price
     * @throws NullPointerException if resource pointer is null
     * @throws IllegalArgumentException if null discount amount
     * Discount amount can be negative, to add customization to the game, but it cannot be zero
     */
    public DiscountAbility(int amount, ResourceSingle resource){

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
    public ResourceSingle getResource() {
        return resource;
    }


    /**
     * function activates the leader ability by adding a discount to the player's available
     * discounts
     * @param player the player who activates the leader card
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public void activate(Player player) {

        if(player == null)
            throw new NullPointerException();
        player.getBoard().getDiscountHolder().addDiscount(resource, amount);

    }
}
