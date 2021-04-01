package it.polimi.ingsw.model.market;

import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.storage.ResourceContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * This object stores the output of a conversion and can transfer the resources/faith from the market to the player
 * basket/faith holder
 */
public class ConversionActuator {

    private final List<ResourceSingle> resourceOutput;
    private final int faithOutput;

    /**
     * Creates a new ConversionActuator.
     * @param resourceOutput the list of resources to be transferred to the player's basket. If empty, this actuator
     *                       doesn't add any resource to the player's basket
     * @param faithOutput the faith points to be added to the player
     */
    public ConversionActuator(List<ResourceSingle> resourceOutput, int faithOutput) {
        if(resourceOutput == null)
            throw new NullPointerException();
        if(faithOutput < 0)
            throw new IllegalArgumentException("Faith cannot be negative");

        this.resourceOutput = resourceOutput;
        this.faithOutput = faithOutput;
    }

    /**
     * Transfers the resources to the player's basket and adds the faith point relative to this conversion
     * @param player the target player of this conversion
     * @throws NullPointerException if player is null
     * @throws IllegalResourceTransferException if resources cannot be added
     */
    public void actuateConversion(Player player) throws IllegalResourceTransferException{
        if(player == null)
            throw new NullPointerException();

        int alreadyTransferred = 0;

        ResourceContainer marketBasket = player.getBoard().getStorage().getMarketBasket();
        FaithHolder faithHolder = player.getBoard().getFaithHolder();

        //try to add all resources to the market basket of the player
        try {
            for (ResourceSingle res : resourceOutput) {
                marketBasket.addResources(res, 1);
                alreadyTransferred++;
            }
        }catch(IllegalResourceTransferException e){
            //if for some reason (shouldn't happen for base storage) resources cannot be transferred, the previous state
            //of the market basket is restored and IllegalResourceTransferException is thrown
            try {
                for (int i = 0; i < alreadyTransferred; i++) {
                    marketBasket.removeResources(resourceOutput.get(i), 1);
                }
            }catch(IllegalResourceTransferException e1){
                throw new UnsupportedOperationException("Logic failed");
            }
            throw new IllegalResourceTransferException("Resources can't be transferred to the player in this conversion");
        }

        //now add the faith points
        faithHolder.addFaithPoints(faithOutput);
    }

    /**
     * Returns the list of the resources relative to this conversion
     * @return the list of the resources relative to this conversion
     */
    public List<ResourceSingle> getResources(){
        return new ArrayList<>(resourceOutput);
    }

    /**
     * Returns the faith points relative to this conversion
     * @return the faith points relative to this conversion
     */
    public int getFaith(){
        return faithOutput;
    }
}

//TODO: Remember to ask for faithHolder and eventually update faithPath instead of faithHolder when adding faith points
//TODO: Maybe we can remove the IllegalResourceTransferException from addResources of BaseStorage