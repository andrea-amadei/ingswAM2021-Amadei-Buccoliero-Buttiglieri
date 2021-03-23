package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.DuplicatedShelfException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.exceptions.UnsupportedShelfInsertionException;
import it.polimi.ingsw.exceptions.UnsupportedShelfRemovalException;
import it.polimi.ingsw.gamematerials.ResourceSingle;

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
     *
     * @return the set of all shelves contained in the cupboard
     */
    @Override
    public Set<Shelf> getShelves() {
        return new HashSet<>(shelves);
    }

    /**
     * Get the shelf with the desired id
     * @param id the id of the desired shelf
     * @return the selected shelf
     * @throws NullPointerException if id is null
     * @throws NoSuchElementException if there is no shelf with the specified id in the cupboard
     */
    @Override
    public Shelf getShelfById(String id) {
        if(id == null)
            throw new NullPointerException();
        return shelves.stream().filter(x->x.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
    }
    /**
     * Moves an amount of resources from one shelf to another
     * @param from the shelf from which resources are taken
     * @param to the shelf to which resources are moved
     * @param amount the amount of resources to transfer
     * @throws NullPointerException if one of the target shelves is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if one of the target shelves isn't contained in the cupboard
     * @throws UnsupportedShelfRemovalException if it is not allowed to remove the resources from the first shelf
     * @throws UnsupportedShelfInsertionException if it is not allowed to insert the resources to the second shelf
     * @throws IllegalCupboardException if upon moving resources, configuration is not valid
     */
    @Override
    public void moveBetweenShelves(Shelf from, Shelf to, int amount) throws IllegalCupboardException{
        if(from == null || to == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException();

        if(!shelves.contains(from) || !shelves.contains(to))
            throw new NoSuchElementException();

        //if the removal is not legal, UnsupportedShelfRemoval is propagated but nothing will happen
        ResourceSingle transferType = from.getCurrentType();
        from.removeResources(amount);

        //if the insertion is not valid, initial state is restored and the UnsupportedShelfException is propagated
        try{
            to.addResources(transferType, amount);
        }catch(UnsupportedShelfInsertionException e){
            from.addResources(transferType, amount);
            throw e;
        }

        //if the new configuration is not valid, initial state is restored and the IllegalCupboardException is thrown
        if(!isValid()){
            to.removeResources(amount);
            from.addResources(transferType, amount);
            throw new IllegalCupboardException();
        }
    }

    /**
     * Add an amount of resources to the specified shelf
     * @param to the shelf to which the resources are added
     * @param resource the type of the resource to add
     * @param amount the amount of resources to add
     * @throws NullPointerException if the target shelf is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if the target shelf isn't contained in the cupboard
     * @throws UnsupportedShelfInsertionException if it is not allowed to insert the resources to the shelf
     * @throws IllegalCupboardException if upon adding resources, configuration is not valid
     */
    @Override
    public void addResource(Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException{
        if(to == null || resource == null)
            throw new NullPointerException();
        if(amount <= 0 )
            throw new IllegalArgumentException();
        if(!contains(to))
            throw new NoSuchElementException();

        //if the resource can't be added to a shelf, UnsupportedShelfInsertion is propagated
        to.addResources(resource, amount);

        //if the new cupboard configuration is not valid, IllegalCupboardException is thrown
        if(!isValid()){
            to.removeResources(amount);
            throw new IllegalCupboardException();
        }
    }

    /**
     * Remove the specified amount from the shelf
     * @param from the shelf from which resources are taken
     * @param amount the amount of resources to remove
     * @throws NullPointerException if the target shelf is null
     * @throws IllegalArgumentException if amount is non-positive
     * @throws NoSuchElementException if the target shelf isn't contained in the cupboard
     */
    @Override
    public void removeResource(Shelf from, int amount) {
        if(from == null)
            throw new NullPointerException();
        if(amount <= 0)
            throw new IllegalArgumentException("amount must be positive");

        if(!contains(from))
            throw new NoSuchElementException();

        from.removeResources(amount);
    }

    /**
     *
     * @return true if the shelf is contained in the cupboard
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
     *
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
