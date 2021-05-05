package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;

import java.util.ArrayList;
import java.util.List;


/**
 * Class ConversionAbility implements SpecialAbilities Interface
 * represents the special ability of a leader card to add a new conversion to the player's
 * list of possible conversions
 */
public class ConversionAbility implements SpecialAbility {
    private final MarbleColor from;
    private final ConversionActuator to;

    /**
     * ConversionAbility Constructor
     * @param from the type of resource to convert (input)
     * @param to the type of resource to convert to (output)
     * @throws NullPointerException if pointer of input resource is null
     * @throws NullPointerException if pointer of output resource is null
     */
    public ConversionAbility(MarbleColor from, ConversionActuator to){

        if (from == null)
            throw new NullPointerException();

        if (to == null)
            throw new NullPointerException();

        this.from = from;
        this.to = to;

    }

    /**
     * @return the marble to convert from
     */
    public MarbleColor getFrom() {
        return from;
    }

    /**
     * @return the resource to convert to
     */
    public ConversionActuator getTo() {
        return to;
    }

    /**
     * function activates the leader ability by adding a new conversion to the player's available
     * conversions
     * @param player the player who activates the leader card
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public List<PayloadComponent> activate(Player player){
        if(player == null)
            throw new NullPointerException();
        player.getBoard().getConversionHolder().addConversionActuator(from, to);

        return new ArrayList<>();
    }

    /**
     * represents the ability as a string
     * @return the conversion ability as a string
     */
    @Override
    public String toString() {
        return "ConversionAbility{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public RawSpecialAbility toRaw() {
        return new RawSpecialAbility(this);
    }
}
