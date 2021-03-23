package it.polimi.ingsw.model.storage;

import it.polimi.ingsw.exceptions.DuplicatedShelfException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.exceptions.UnsupportedLeaderShelfOperation;
import it.polimi.ingsw.gamematerials.ResourceSingle;

import java.util.NoSuchElementException;
import java.util.Set;

public class LeaderDecorator implements Cupboard{
    private final Cupboard decoratedCupboard;
    private final Shelf leaderShelf;


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

    @Override
    public Set<Shelf> getShelves() {
        Set<Shelf> innerShelves = decoratedCupboard.getShelves();
        innerShelves.add(leaderShelf);
        return innerShelves;
    }

    @Override
    public Shelf getShelfById(String id) {
        Shelf requestedShelf;
        try{
            requestedShelf = decoratedCupboard.getShelfById(id);
            return requestedShelf;
        }catch(NoSuchElementException e){
            if(leaderShelf.getId().equals(id))
                return leaderShelf;
        }

        throw new NoSuchElementException();
    }

    @Override
    public void moveBetweenShelves(Shelf from, Shelf to, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.moveBetweenShelves(from, to, amount);
        }catch(NoSuchElementException e){
            if(from.equals(leaderShelf) && decoratedCupboard.contains(to))
                throw new UnsupportedLeaderShelfOperation("Resources cannot be moved from leader shelf to another shelf");
            throw new NoSuchElementException();
        }
    }

    @Override
    public void addResource(Shelf to, ResourceSingle resource, int amount) throws IllegalCupboardException {
        try{
            decoratedCupboard.addResource(to, resource, amount);
        }catch(NoSuchElementException e){
            if(!leaderShelf.equals(to))
                throw new NoSuchElementException();

            //if the resource can't be added to the shelf, UnsupportedShelfInsertion is propagated
            leaderShelf.addResources(resource, amount);
        }
    }

    @Override
    public void removeResource(Shelf from, int amount) {
        try{
            decoratedCupboard.removeResource(from, amount);
        }catch(NoSuchElementException e){
            if(!leaderShelf.equals(from)){
                throw new NoSuchElementException();
            }

            //if the resource can't be removed to the shelf, UnsupportedShelfInsertion is propagated
            leaderShelf.removeResources(amount);
        }
    }

    @Override
    public boolean contains(Shelf shelf) {
        return decoratedCupboard.contains(shelf) || leaderShelf.equals(shelf);
    }

    @Override
    public String toString(){
        return "{" + decoratedCupboard.toString() + ", " + leaderShelf.toString() + "}";
    }
}
