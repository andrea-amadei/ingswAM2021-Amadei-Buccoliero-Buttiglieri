package it.polimi.ingsw.model.leader;

import java.util.List;

/**
 * Class LeaderCard allows to instantiate leader cards and use them in game
 */
public class LeaderCard {

    private final int id;
    private final String name;
    private boolean status;
    private final int points;
    private final List<SpecialAbility> abilities;
    private final List<Requirement> requirements;

    /**
     * LeaderCard constuctor
     * @param id the card ID number
     * @param name the name of the card
     * @param points the total amount of victory points of the card
     * @param abilities a List that contains all the abilities of the card
     * @param requirements a List that contains all the requirements to satisfy
     *                     in order to activate the card
     * @throws IllegalArgumentException if negative ID number
     * @throws IllegalArgumentException if name length is too short or too long
     * @throws IllegalArgumentException if negative victory points
     * @throws NullPointerException if abilities pointer is null
     * @throws NullPointerException if requirements pointer is null
     */
    public LeaderCard (int id, String name, int points, List<SpecialAbility> abilities, List<Requirement> requirements){

        if(id < 0)
            throw new IllegalArgumentException("Id number cannot be negative");

        if(name.length()<2 || name.length()>30)
            throw new IllegalArgumentException("Leader name length is out of bound");

        if(points < 0)
            throw new IllegalArgumentException("Victory points cannot be negative");

        if(abilities == null)
            throw new NullPointerException();

        if(requirements == null)
            throw new NullPointerException();

        this.id = id;
        this.name = name;
        status = false;
        this.points = points;
        this.abilities = abilities;
        this.requirements = requirements;
    }

    //TODO: activate function

    //TODO: isSatisfied function


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
     * get status
     * @return the current status of the leader card (active or non active)
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
        return abilities;
    }

    /**
     * get requirements
     * @return the List that contains all requirements needed to activate the card
     */
    public List<Requirement> getRequirements() {
        return requirements;
    }
}
