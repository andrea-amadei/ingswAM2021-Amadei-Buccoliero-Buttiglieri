package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.client.observables.Observable;
import it.polimi.ingsw.parser.raw.RawLeaderCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientLeaderCards implements Observable<ClientLeaderCards> {

    private final List<Listener<ClientLeaderCards>> listeners;

    private final List<RawLeaderCard> leaderCards;
    private final Set<Integer> activatedLeaderCardIndexes;
    private int coveredCards;

    /**
     * Creates a new ClientLeaderCards object. The list of leader cards is empty and
     * the number of coveredCards is 0
     */
    public ClientLeaderCards(){
        this.leaderCards = new ArrayList<>();
        this.activatedLeaderCardIndexes = new HashSet<>();
        this.coveredCards = 0;

        this.listeners = new ArrayList<>();
    }

    public void addLeaderCard(RawLeaderCard card){
        leaderCards.add(card);
        update();
    }

    public void removeLeaderCard(int index){
        leaderCards.remove(index);
        update();
    }

    public void activateLeaderCard(int index){
        activatedLeaderCardIndexes.add(index);
        update();
    }

    public void activateLeaderCardsById(int id){
        RawLeaderCard toActivate = leaderCards.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        activateLeaderCard(leaderCards.indexOf(toActivate));
    }

    public void changeCoveredCardsNumber(int delta){
        coveredCards += delta;
        update();
    }

    public void removeLeaderCardById(int id){
        RawLeaderCard toRemove = leaderCards.stream()
                               .filter(x -> x.getId() == id)
                               .findFirst()
                               .orElseThrow(IllegalArgumentException::new);

        removeLeaderCard(leaderCards.indexOf(toRemove));
    }

    public List<RawLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public Set<Integer> getActivatedLeaderCardIndexes() {
        return activatedLeaderCardIndexes;
    }

    public int getCoveredCards() {
        return coveredCards;
    }

    @Override
    public void addListener(Listener<ClientLeaderCards> listener) {
        listeners.add(listener);
    }

    @Override
    public void update() {
        for(Listener<ClientLeaderCards> l : listeners)
            l.update(this);
    }
}
