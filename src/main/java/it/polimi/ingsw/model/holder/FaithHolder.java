package it.polimi.ingsw.model.holder;

import it.polimi.ingsw.exceptions.AlreadyReachedPopeCardException;
import it.polimi.ingsw.exceptions.UsedUnreachedPopeCardException;

import java.util.ArrayList;
import java.util.List;

/**
 * The FaithHolder class holds the information about Faith and Pope Cards for a player.
 */
public class FaithHolder {
    private int faithPoints;
    private final List<CheckpointStatus> checkpoints;

    public enum CheckpointStatus {
        UNREACHED,
        INACTIVE,
        ACTIVE
    }

    public FaithHolder(int checkpointsNumber){
        faithPoints = 0;
        checkpoints = new ArrayList<>(checkpointsNumber);

        for(int i = 0; i < checkpointsNumber; i++)
            checkpoints.add(CheckpointStatus.UNREACHED);
    }

    /**
     * @return the amount of faith points
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Returns if a given Pope Card was reached before
     * @param index the index of the pope card
     * @return true if the card was reached before, false otherwise
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public boolean isPopeCardReached(int index) {
        if(index < 0 || index > checkpoints.size() - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (checkpoints.size() - 1));

        return !checkpoints.get(index).equals(CheckpointStatus.UNREACHED);
    }

    /**
     * Returns if a given Pope Card is set to active
     * @param index the index of the pope card
     * @return true if the card is active, false if inactive or never reached
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public boolean isPopeCardActive(int index) {
        if(index < 0 || index > checkpoints.size() - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (checkpoints.size() - 1));

        return checkpoints.get(index).equals(CheckpointStatus.ACTIVE);
    }

    /**
     * Returns the status of the pope card at the specified index
     * @param index the index of the pope card
     * @return the status of the pope card at the specified index
     */
    public CheckpointStatus getPopeCardStatus(int index){
        if(index < 0 || index > checkpoints.size() - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (checkpoints.size() - 1));

        return checkpoints.get(index);
    }

    /**
     * Sets a given Pope Card to active
     * @param index the index of the pope card
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws UsedUnreachedPopeCardException if the pope card was unreached
     */
    public void setPopeCardActive(int index) {
        if(index < 0 || index > checkpoints.size() - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (checkpoints.size() - 1));

        if(checkpoints.get(index).equals(CheckpointStatus.UNREACHED))
            checkpoints.set(index, CheckpointStatus.ACTIVE);
        else
            throw new AlreadyReachedPopeCardException("Pope card " + index + " wasn't unreached, cannot set it to active");
    }

    /**
     * Sets a given Pope Card to inactive
     * @param index the index of the pope card
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws UsedUnreachedPopeCardException if the pope card was unreached
     */
    public void setPopeCardInactive(int index) {
        if(index < 0 || index > checkpoints.size() - 1)
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (checkpoints.size() - 1));

        if(checkpoints.get(index).equals(CheckpointStatus.UNREACHED))
            checkpoints.set(index, CheckpointStatus.INACTIVE);
        else
            throw new UsedUnreachedPopeCardException("Pope card " + index + " wasn't unreached, cannot set it to inactive");
    }

    /**
     * Resets faith points to 0
     */
    public void resetFaithPoints() {
        faithPoints = 0;
    }

    /**
     * Adds a given amount of faith points
     * @param amount the amount of points to be added. Cannot be negative
     * @throws IllegalArgumentException if the amount is negative
     */
    public void addFaithPoints(int amount) {
        if(amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");

        faithPoints += amount;
    }
}
