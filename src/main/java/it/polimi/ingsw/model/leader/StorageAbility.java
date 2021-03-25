package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.storage.Shelf;

/**
 * Class StorageAbility implements SpecialAbility interface
 * represents leader card ability to add extra storage space to the player's storage
 */
public class StorageAbility implements SpecialAbility {

    private final Shelf shelf;

    /**
     * StorageAbility Constructor
     * @param shelf the pointer to the extra shelf
     * @throws NullPointerException if shelf pointer is null
     */
    public StorageAbility(Shelf shelf){

        if(shelf == null)
            throw new NullPointerException();

        this.shelf = shelf;
    }

    /**
     * get shelf
     * @return the pointer to the extra shelf
     */
    public Shelf getShelf(){
        return shelf;
    }


    //activate function
    @Override
    public void activate(Player player) {

        //TODO: activate function

    }
}
