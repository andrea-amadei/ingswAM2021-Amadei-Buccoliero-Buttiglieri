package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ConversionAbility implements SpecialAbilities Interface
 * represents the special ability of a leader card to add a new conversion to the player's
 * list of possible conversions
 */
public class ConversionAbility implements SpecialAbility {
    private final MarbleColor from;
    private final ResourceSingle to;

    /**
     * ConversionAbility Constructor
     * @param from the type of resource to convert (input)
     * @param to the type of resource to convert to (output)
     * @throws NullPointerException if pointer of input resource is null
     * @throws NullPointerException if pointer of output resource is null
     */
    public ConversionAbility(MarbleColor from, ResourceSingle to){

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
    public ResourceSingle getTo() {
        return to;
    }

    // activate function
    @Override
    public void activate(Player player){

    //TODO: activate function

    }
}
