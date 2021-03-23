package it.polimi.ingsw.model.leader;

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

    //TODO: pensare. Lista? oppure map? oppure LeaderCard ha una lista?
    private final ResourceSingle from;
    private final ResourceSingle to;

    /**
     * ConversionAbility Constructor
     * @param from the type of resource to convert (input)
     * @param to the type of resource to convert to (output)
     * @throws NullPointerException if pointer of input resource is null
     * @throws NullPointerException if pointer of output resource is null
     */
    public ConversionAbility(ResourceSingle from, ResourceSingle to){

        if (from == null)
            throw new NullPointerException();

        if (to == null)
            throw new NullPointerException();

        this.from = from;
        this.to = to;

    }

    /**
     * get input and output resources of the conversion
     * @return the List of input and output resources of the conversion
     */
    public List<ResourceSingle> getResources(){
        List <ResourceSingle> temporary = new ArrayList<>();
        temporary.add(from);
        temporary.add(to);

        return temporary;

    }

    // activate function
    @Override
    public void activate(Player player){

    //TODO: activate function

    }
}
