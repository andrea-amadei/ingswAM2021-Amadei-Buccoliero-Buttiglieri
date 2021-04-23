package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.storage.LeaderDecorator;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;

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


    /**
     * function activates the leader ability to give the player extra storage space
     * by adding a new shelf to the player's cupboard
     * @param player the player who activates the leader card
     * @throws NullPointerException if the pointer to player is null
     */
    @Override
    public void activate(Player player) {

        if(player == null)
            throw new NullPointerException();

        LeaderDecorator leaderDecorator = new LeaderDecorator(shelf, player.getBoard().getStorage().getCupboard());
        player.getBoard().getStorage().decorate(leaderDecorator);

    }

    @Override
    public String toString() {
        return "StorageAbility{" +
                "shelf=" + shelf +
                '}';
    }

    @Override
    public RawSpecialAbility toRaw() {
        return new RawSpecialAbility(this);
    }
}
