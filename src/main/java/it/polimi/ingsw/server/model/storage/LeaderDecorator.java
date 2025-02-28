package it.polimi.ingsw.server.model.storage;

import it.polimi.ingsw.common.exceptions.*;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.Logger;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class decorates a cupboard adding new shelves.
 * The added shelves are not considered for the BaseCupboard invariant (see game rules)
 */
public class LeaderDecorator implements Cupboard{
    private final Cupboard decoratedCupboard;
    private final Shelf leaderShelf;

    /**
     * A new empty shelf is added to the cupboard
     * @param leaderShelf the shelf added to the cupboard
     * @param decoratedCupboard the cupboard being decorated
     * @throws NullPointerException if leaderShelf or decoratedCupboard are null
     * @throws DuplicatedShelfException if the new shelf has the same id of a shelf already in the cupboard
     * @throws IllegalArgumentException if the new shelf is not empty
     */
    public LeaderDecorator(Shelf leaderShelf, Cupboard decoratedCupboard){
        if(leaderShelf == null || decoratedCupboard == null)
            throw new NullPointerException();
        if(decoratedCupboard.getShelves().stream().anyMatch(s -> s.getId().equals(leaderShelf.getId())))
            throw new DuplicatedShelfException("Can't add a leader shelf with an already used Id");
        if(leaderShelf.getAmount() > 0)
            throw new IllegalArgumentException("The leader shelf needs to be empty when added to the cupboard");

        this.decoratedCupboard = decoratedCupboard;
        this.leaderShelf = leaderShelf;
    }


    /**
     * Returns the set of all the shelves contained in the cupboard
     * @return the set of all the shelves contained in the cupboard
     */
    @Override
    public Set<Shelf> getShelves() {
        Set<Shelf> innerShelves = decoratedCupboard.getShelves();
        innerShelves.add(leaderShelf);
        return innerShelves;
    }

    /**
     * Gets the shelf with the desired id (case insensitive)
     * @param id the id of the desired shelf
     * @return the selected shelf
     * @throws NullPointerException if id is null
     * @throws NoSuchElementException if there is no shelf with the specified id in the cupboard
     */
    @Override
    public Shelf getShelfById(String id) {
        Shelf requestedShelf;
        try{
            requestedShelf = decoratedCupboard.getShelfById(id);
            return requestedShelf;
        }catch(NoSuchElementException e){
            if(leaderShelf.getId().equalsIgnoreCase(id))
                return leaderShelf;
        }

        throw new NoSuchElementException("There is no shelf with ID " + id);
    }

    /**
     * Moves an amount of resources from one shelf to another. The transaction is invalid if a leaderShelf is involved (check game rules)
     * @param from the shelf from which resources are taken
     * @param to the shelf to which resources are moved
     * @param amount the amount of resources to transfer
     * @throws NullPointerException if one of the target shelves is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if one of the target shelves isn't contained in the cupboard
     * @throws IllegalCupboardException if upon moving resources, configuration is not valid, or if the transaction is not possible
     */
    @Override
    public void moveBetweenShelves(Shelf from, Shelf to, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.moveBetweenShelves(from, to, amount);
        }catch(NoSuchElementException e){
            //It means that resources must be removed from this leader shelf and must be added to the underlying cupboard
            if(from.equals(leaderShelf) && decoratedCupboard.contains(to)) {
                ResourceSingle resourceTypeToMove = from.getCurrentType();

                //Try to remove resources from the leader shelf. If not possible, IllegalCupboardException is thrown
                try{
                    leaderShelf.removeResources(amount);
                } catch (IllegalResourceTransferException illegalResourceTransferException) {
                    throw new IllegalCupboardException("Resources cannot be moved from the leader shelf");
                }

                //try to add resources to the underlying cupboard. If not possible, resources are inserted again in the
                //leader shelf and IllegalCupboardException is thrown
                try {
                    decoratedCupboard.addResource(to, resourceTypeToMove, amount);
                } catch (IllegalCupboardException e1) {
                    try {
                        leaderShelf.addResources(resourceTypeToMove, amount);
                    } catch (IllegalResourceTransferException resourceTransferException) {
                        Logger.log("Logic failed when moving resources from a leader shelf to another shelf that could not" +
                                "receive those resources", Logger.Severity.ERROR);
                        throw new IllegalCupboardException("Logic failed when moving resources from a leader shelf to another shelf that could not" +
                                "receive those resources");
                    }
                    throw e1;
                }
            //It means that resources must be added to this leader shelf and must be removed from the underlying cupboard
            }else if(to.equals(leaderShelf) && decoratedCupboard.contains(from)){
                ResourceSingle resourceTypeToMove = decoratedCupboard.getShelfById(from.getId()).getCurrentType();

                //Try to remove resources from the cupboard. If it is impossible to remove those resources,
                //IllegalCupboardException is propagated
                decoratedCupboard.removeResource(from, amount);

                //Try to add resources to this leaderShelf. If it is not possible, we restore the shelf from which
                //resource have been taken and throw an IllegalCupboardException
                try{
                    leaderShelf.addResources(resourceTypeToMove, amount);
                } catch (IllegalResourceTransferException e1) {
                    try {
                        decoratedCupboard.addResource(from, resourceTypeToMove, amount);
                    }catch(IllegalCupboardException e2){
                        Logger.log("Logic failed when moving resources from a shelf to a leader shelf that could not" +
                                "receive those resources", Logger.Severity.ERROR);
                        throw new IllegalCupboardException("Logic failed when moving resources from a shelf to a leader shelf that could not" +
                                "receive those resources");

                    }
                    throw new IllegalCupboardException(e1.getMessage());
                }
            }else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Adds an amount of resources to the specified shelf
     * @param to the shelf to which the resources are added
     * @param resource the type of the resource to add
     * @param amount the amount of resources to add
     * @throws NullPointerException if the target shelf is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if the target shelf isn't contained in the cupboard
     * @throws IllegalCupboardException if upon adding resources, configuration is not valid or if the transaction can't be performed
     */
    @Override
    public void addResource(Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.addResource(to, resource, amount);
        }catch(NoSuchElementException e){
            if(!leaderShelf.equals(to))
                throw new NoSuchElementException();

            //if the resources can't be added to the shelf, IllegalCupboardException is propagated
            try {
                leaderShelf.addResources(resource, amount);
            } catch (IllegalResourceTransferException e2) {
                throw new IllegalCupboardException("Resources can't be added to the shelf");
            }
        }
    }


    /**
     * Adds an amount of resources to the specified shelf from the specified ResourceContainer
     *
     * @param container the ResourceContainer from which resources are taken
     * @param to        the shelf to which the resources are added
     * @param resource  the type of the resource to add
     * @param amount    the amount of resources to add
     * @throws NullPointerException if parameters are null
     * @throws IllegalArgumentException if amount <= 0
     * @throws NoSuchElementException if the cupboard doesn't contain the shelf
     * @throws IllegalCupboardException if upon adding resources, configuration is not valid or the transaction can't be performed
     */
    @Override
    public void addResourceFromContainer(ResourceContainer container, Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.addResourceFromContainer(container, to, resource, amount);
        }catch(NoSuchElementException e){
            if(!leaderShelf.equals(to))
                throw new NoSuchElementException("Container " + container.getId() + " does not contain " + amount +
                        " " + resource.getId());

            //if the resources can't be added to the shelf, IllegalCupboardException is propagated
            try{
                container.moveTo(leaderShelf, resource, amount);
            }catch(IllegalResourceTransferException e1){
                throw new IllegalCupboardException("Resources can't be added to the shelf");
            }
        }
    }

    /**
     * Removes the specified amount from the shelf
     * @param from the shelf from which resources are taken
     * @param amount the amount of resources to remove
     * @throws NullPointerException if the target shelf is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if the target shelf isn't contained in the cupboard
     * @throws IllegalCupboardException if the transaction can't be performed
     */
    @Override
    public void removeResource(Shelf from, int amount) throws IllegalCupboardException{
        try{
            decoratedCupboard.removeResource(from, amount);
        }catch(NoSuchElementException e){
            if(!leaderShelf.equals(from)){
                throw new NoSuchElementException();
            }

            //if the resource can't be removed to the shelf, IllegalCupboardException is propagated
            try {
                leaderShelf.removeResources(amount);
            } catch (IllegalResourceTransferException e2) {
                throw new IllegalCupboardException("Can't remove resources from the shelf");
            }
        }
    }

    /**
     * Moves resources from a shelf to a container
     *
     * @param container the ResourceContainer to which resources are added
     * @param from      the Shelf from which resources are removed
     * @param resource  the resource type of the transferred resources
     * @param amount    the amount of the transferred resources
     * @throws NullPointerException if the parameters are null
     * @throws IllegalArgumentException if the amount is <= 0
     * @throws NoSuchElementException if the cupboard doesn't contain the shelf
     * @throws IllegalCupboardException if the transaction can't be performed
     */
    @Override
    public void moveResourceToContainer(ResourceContainer container, Shelf from, ResourceSingle resource, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.moveResourceToContainer(container, from, resource, amount);
        } catch(NoSuchElementException e){
            if(!(leaderShelf.equals(from)))
                throw new NoSuchElementException();

            //if the resource can't be removed to the shelf, IllegalCupboardException is propagated
            try{
                from.moveTo(container, resource, amount);
            }catch(IllegalResourceTransferException e1){
                throw new IllegalCupboardException("Can't remove resources from the shelf");
            }
        }
    }

    /**
     * Returns true iff the shelf is contained in the cupboard
     * @return true iff the shelf is contained in the cupboard
     */
    @Override
    public boolean contains(Shelf shelf) {
        return decoratedCupboard.contains(shelf) || leaderShelf.equals(shelf);
    }

    /**
     * Returns the representation of this cupboard"
     * @return the representation of this cupboard"
     */
    @Override
    public String toString(){
        return "{" + decoratedCupboard.toString() + ", " + leaderShelf.toString() + "}";
    }
}
