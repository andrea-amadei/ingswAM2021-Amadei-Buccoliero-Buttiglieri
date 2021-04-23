package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.exceptions.AlreadyActiveException;
import it.polimi.ingsw.exceptions.RequirementsNotSatisfiedException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.SerializableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class LeaderCard allows to instantiate leader cards and use them in game
 */
public class LeaderCard implements SerializableObject {

    private final int id;
    private final String name;
    private final int points;
    private final List<SpecialAbility> abilities;
    private final List<Requirement> requirements;
    private boolean status;

    /**
     * LeaderCard constructor
     * @param id the card ID number
     * @param name the name of the card
     * @param points the total amount of victory points of the card
     * @param abilities a List that contains all the abilities of the card
     * @param requirements a List that contains all the requirements to satisfy
     *                     in order to activate the card
     * @throws IllegalArgumentException if negative ID number
     * @throws IllegalArgumentException if name length is too short or too long
     * @throws IllegalArgumentException if negative victory points
     * @throws IllegalArgumentException if abilities or requirements are empty
     * @throws NullPointerException if abilities is null
     * @throws NullPointerException if requirements is null
     * @throws NullPointerException if name is null
     */
    public LeaderCard (int id, String name, int points, List<SpecialAbility> abilities, List<Requirement> requirements){

        if(id <= 0)
            throw new IllegalArgumentException("Id number must be positive");

        if(name == null)
            throw new NullPointerException();

        if(name.length() < 2 || name.length() > 30)
            throw new IllegalArgumentException("Leader name length is out of bounds");

        if(points < 0)
            throw new IllegalArgumentException("Victory points cannot be negative");

        if(abilities == null)
            throw new NullPointerException();

        if(requirements == null)
            throw new NullPointerException();

        if(abilities.size() == 0 || requirements.size() == 0)
            throw new IllegalArgumentException("Abilities and requirements cannot be empty");

        this.id = id;
        this.name = name;
        status = false;
        this.points = points;
        this.abilities = abilities;
        this.requirements = requirements;
    }

    /**
     * method activates all leader card abilities and sets the card status to true
     * @param player the player who activates the leader card
     * @throws NullPointerException if pointer to player is null
     * @throws RequirementsNotSatisfiedException if a player tries to activate a leader card whose
     * requirements are not satisfied
     */
    public void activate(Player player) throws RequirementsNotSatisfiedException, AlreadyActiveException {
        if(player == null)
            throw new NullPointerException();
        if(!LeaderCard.this.isSatisfied(player))
            throw new RequirementsNotSatisfiedException();
        if(LeaderCard.this.status)
            throw new AlreadyActiveException();

        for(SpecialAbility specialAbility : abilities){
            specialAbility.activate(player);
        }
        status = true;

    }

    /**
     * method verifies if the player satisfies all leader card requirements
     * @param player the player who is being verified
     * @return true iff all leader card requirements are satisfied, false otherwise
     * @throws NullPointerException if pointer to player is null
     */
    public boolean isSatisfied(Player player){
        if(player == null)
            throw new NullPointerException();

        for( Requirement requirement : requirements) {
            if (!requirement.isSatisfied(player)){
                return false;
            }
        }
        return true;
    }


    /**
     * get ID
     * @return the ID number
     */
    public int getId(){
        return id;
    }

    /**
     * get name
     * @return the name of the leader card
     */
    public String getName(){
        return name;
    }

    /**
     * checks the current status of the leader card (active or non active)
     * @return true iff the leader card is currently active, false otherwise
     */
    public boolean isActive(){
        return status;
    }

    /**
     * get victory points
     * @return the amount of victory points
     */
    public int getPoints(){
        return points;
    }

    /**
     * get special abilities
     * @return the List that contains all special abilities of the card
     */
    public List<SpecialAbility> getAbilities() {
        return new ArrayList<>(abilities);
    }

    /**
     * get requirements
     * @return the List that contains all requirements needed to activate the card
     */
    public List<Requirement> getRequirements() {
        return new ArrayList<>(requirements);
    }

    @Override
    public void printDebugInfo() {
        Console.log("Id: " + getId());
        Console.log("Name: " + getName());
        Console.log("Points: " + getPoints());

        Console.log("Requirements: " + getRequirements().size());
        for(Requirement i : getRequirements())
            Console.log("  - " + i.toString());

        Console.log("Special abilities: " + getAbilities().size());
        for(SpecialAbility i : getAbilities())
            Console.log("  - " + i.toString());
    }
}
