package it.polimi.ingsw.model.market;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.storage.ResourceContainer;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.HashMap;
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
     * @throws NullPointerException if resourceOutput is null
     * @throws IllegalArgumentException if faithOutput is negative
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
     * @param faithPath the faithPath that will be updated with the added faith points
     * @throws NullPointerException if player or faithPath is null
     * @throws IllegalResourceTransferException if resources cannot be added
     */
    public List<PayloadComponent> actuateConversion(Player player, FaithPath faithPath) throws IllegalResourceTransferException{
        if(player == null || faithPath == null)
            throw new NullPointerException();

        List<PayloadComponent> payload = new ArrayList<>();

        int alreadyTransferred = 0;

        ResourceContainer marketBasket = player.getBoard().getStorage().getMarketBasket();

        //try to add all resources to the market basket of the player
        try {
            for (ResourceSingle res : resourceOutput) {
                marketBasket.addResources(res, 1);
                alreadyTransferred++;
                payload.add(PayloadFactory.changeResources(player.getUsername(),
                        new RawStorage("MarketBasket", new HashMap<>(){{
                            put(res.toString().toLowerCase(), 1);
                        }})));
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
        if(faithOutput > 0)
            payload.addAll(faithPath.executeMovement(faithOutput, player));

        return payload;
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

    @Override
    public String toString() {
        return "ConversionActuator{" +
                "resourceOutput=" + resourceOutput +
                ", faithOutput=" + faithOutput +
                '}';
    }
}