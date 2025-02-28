package it.polimi.ingsw.server.model.storage;

import it.polimi.ingsw.common.exceptions.*;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;

import java.util.*;

/**
 * This is the base cupboard of the player. It contains a fixed amount of shelves.
 * For every shelf s, if s is not empty, then there cannot be another shelf s1 != s such that s and s1 contains
 * the same type of resource.
 */
public class BaseCupboard implements Cupboard{

    private final List<Shelf> shelves;

    /**
     * A new cupboard is created. All shelves must be empty and must have different ids
     * @param shelves the list of shelves contained in the cupboard
     * @throws NullPointerException if shelves is null
     * @throws IllegalArgumentException if there are some null or nonempty shelves
     * @throws DuplicatedShelfException if two shelves has the same id
     */
    public BaseCupboard(List<Shelf> shelves){
        if(shelves == null)
            throw new NullPointerException();

        //check for null shelves
        if(shelves.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Shelves can't be null");

        //check for empty list of shelves
        if(shelves.size() == 0)
            throw new IllegalArgumentException("There must be at least one shelf in the base cupboard");

        //check for duplicates
        Set<String> distinct = new HashSet<>();
        if(shelves.stream().anyMatch(s -> !distinct.add(s.getId()))){
            throw new DuplicatedShelfException("Can't add two shelves with same id");
        }

        //check if shelves are empty
        if(shelves.stream().anyMatch(s -> s.getAmount() > 0))
            throw new IllegalArgumentException("Only empty shelves can be added to cupboard");

        this.shelves = shelves;
    }


    /**
     * Returns the set of all the shelves contained in the cupboard
     * @return the set of all the shelves contained in the cupboard
     */
    @Override
    public Set<Shelf> getShelves() {
        return new HashSet<>(shelves);
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
        if(id == null)
            throw new NullPointerException();
        return shelves.stream().filter(x-> x.getId().equalsIgnoreCase(id)).findFirst().orElseThrow(()->
                new NoSuchElementException("There is no shelf with ID " + id));
    }
    /**
     * Moves an amount of resources from one shelf to another
     * @param from the shelf from which resources are taken
     * @param to the shelf to which resources are moved
     * @param amount the amount of resources to transfer
     * @throws NullPointerException if one of the target shelves is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if one of the target shelves isn't contained in the cupboard
     * @throws IllegalCupboardException if upon moving resources, configuration is not valid, or if the transaction is not possible
     */
    @Override
    public void moveBetweenShelves(Shelf from, Shelf to, int amount) throws IllegalCupboardException{
        if(from == null || to == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException();

        if(!shelves.contains(from) || !shelves.contains(to))
            throw new NoSuchElementException();

        if(from.getCurrentType() == null)
            throw new IllegalCupboardException("Trying to remove resources from an empty shelf");

        try{
            from.moveTo(to, from.getCurrentType(), amount);
        }catch(IllegalResourceTransferException e){
            throw new IllegalCupboardException("Can't transfer the resources");
        }

        //if the new configuration is not valid, initial state is restored and the IllegalCupboardException is thrown
        if(!isValid()){
            try{
                to.moveTo(from, to.getCurrentType(), amount);
            }catch(IllegalResourceTransferException e){
                throw new IllegalArgumentException();
            }
            throw new IllegalCupboardException("Cupboard configuration would not be valid");
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
     * @throws IllegalCupboardException if upon adding resources, configuration is not valid or the transaction can't be performed
     */
    @Override
    public void addResource(Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException{
        if(to == null || resource == null)
            throw new NullPointerException();
        if(amount <= 0 )
            throw new IllegalArgumentException();
        if(!contains(to))
            throw new NoSuchElementException();

        try{
            to.addResources(resource, amount);
        }catch(IllegalResourceTransferException e){
            throw new IllegalCupboardException("Transaction is not valid");
        }

        //if the new cupboard configuration is not valid, IllegalCupboardException is thrown
        if(!isValid()){
            try {
                to.removeResources(amount);
            }catch(IllegalResourceTransferException e){
                throw new IllegalCupboardException("Error while restoring original state");
            }
            throw new IllegalCupboardException("Cupboard configuration would not be valid");
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
        if(to == null || resource == null || container == null)
            throw new NullPointerException();
        if(amount <= 0 )
            throw new IllegalArgumentException("Cannot add a negative amount of resources");
        if(!contains(to))
            throw new NoSuchElementException("Container " + container.getId() + " does not contain " + amount +
                    " " + resource.getId());

        try{
            container.moveTo(to, resource, amount);
        } catch (IllegalResourceTransferException e) {
            throw new IllegalCupboardException("Can't add resources from the container");
        }


        //if the new cupboard configuration is not valid, IllegalCupboardException is thrown
        if(!isValid()){
            try {
                to.moveTo(container, resource, amount);
            }catch(IllegalResourceTransferException e){
                throw new IllegalCupboardException("Error while restoring original state");
            }
            throw new IllegalCupboardException("Cupboard configuration would not be valid");
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
    public void removeResource(Shelf from, int amount) throws IllegalCupboardException {
        if(from == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if(!contains(from))
            throw new NoSuchElementException();

        try {
            from.removeResources(amount);
        } catch (IllegalResourceTransferException e) {
            throw new IllegalCupboardException("Resource transaction cannot be performed");
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
        if(container == null || from == null || resource == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("The amount must be positive");
        if(!contains(from))
            throw new NoSuchElementException("Container " + from.getId() + " does not contain " + amount + " " + resource.getId());

        try{
            from.moveTo(container, resource, amount);
        }catch(IllegalResourceTransferException e ){
            throw new IllegalCupboardException("Resource transaction cannot be performed");
        }
    }

    /**
     * Returns true iff the shelf is contained in the cupboard
     * @return true iff the shelf is contained in the cupboard
     */
    @Override
    public boolean contains(Shelf shelf) {
        return shelves.contains(shelf);
    }

    private boolean isValid(){

        //since each shelf state is valid, we only need to check that all non-null currentType are different

        Set<ResourceSingle> checkSet = new HashSet<>();
        return shelves.stream()
                      .map(Shelf::getCurrentType)
                      .filter(Objects::nonNull)
                      .allMatch(checkSet::add);
    }

    /**
     * Returns the representation of this cupboard: es "BaseCupboard{Id1 {}, Id2 {Gold: 3}}"
     * @return the representation of this cupboard: es "BaseCupboard {Id1 {}, Id2 {Gold: 3}}"
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("BaseCupboard {");
        for(int i = 0; i < shelves.size(); i++){
            sb.append(shelves.get(i));
            if(i < shelves.size() -1){
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
