package it.polimi.ingsw.server.model.leader;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class CraftingAbility implements SpecialAbility interface
 * represents leader card ability to add a new crafting scheme to the player's available
 * crafting schemes
 */
public class CraftingAbility implements SpecialAbility {

    private final Crafting crafting;

    /**
     * CraftingAbility Constructor
     * @param crafting the crafting to assign as a special ability
     * @throws NullPointerException if crafting pointer is null
     */
    public CraftingAbility (Crafting crafting){

        if(crafting == null)
            throw new NullPointerException();

        this.crafting = crafting;

    }

    /**
     * get crafting
     * @return the crafting
     */
    public Crafting getCrafting(){
        return crafting;
    }

    /**
     * function activates the leader ability by adding a new crafting to the player's available
     * crafting
     * @param player the player who activates the leader card
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public List<PayloadComponent> activate(Player player) {
        List<PayloadComponent> payload = new ArrayList<>();

        if(player == null)
            throw new NullPointerException();
        player.getBoard().getProduction().addLeaderCrafting(crafting);

        Integer index = player.getBoard().getProduction().getAllLeaderCrafting().size() - 1;
        payload.add(PayloadFactory.addCrafting(player.getUsername(), crafting.toRaw(), Production.CraftingType.LEADER, index));

        return payload;
    }

    /**
     * function represents the ability as a string
     * @return the crafting ability as a string
     */
    @Override
    public String toString() {
        return "CraftingAbility{" +
                "crafting=" + crafting +
                '}';
    }

    @Override
    public RawSpecialAbility toRaw() {
        return new RawSpecialAbility(this);
    }
}
