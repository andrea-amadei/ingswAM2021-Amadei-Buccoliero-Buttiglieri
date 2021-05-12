package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clienthandling.Match;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The manager of the games in the server. It handles the creation of new Matches and stores them
 */
public class MatchesManager {
    private final Set<Match> matches;
    private final List<String> registeredUsernames;

    public MatchesManager(){
        matches = new HashSet<>();
        registeredUsernames = new ArrayList<>();
    }

    public synchronized Set<Match> getMatches(){
        return new HashSet<>(matches);
    }

    public synchronized boolean alreadyExistentUsername(String username){
        return matches.stream().anyMatch(m -> m.getUsernames().contains(username));
    }

    public synchronized boolean alreadyExistentGameName(String gameName){
        return matches.stream().anyMatch(m -> m.getGameName().equals(gameName));
    }

    public synchronized void addMatch(Match match){
        if(match == null)
            throw new NullPointerException();
        if(alreadyExistentGameName(match.getGameName()))
            throw new IllegalArgumentException("There is another match with name \"" + match.getGameName() + "\"");
        if(match.getUsernames().stream().anyMatch(this::alreadyExistentUsername))
            throw new IllegalArgumentException("There is at least a client that has the same username of another client in the server");

        matches.add(match);
    }

    /**
     *
     * @param username the username to add
     * @throws IllegalArgumentException if another username is already registered
     */
    public synchronized void registerUsername(String username){
        if(registeredUsernames.contains(username))
            throw new IllegalArgumentException();

        registeredUsernames.add(username);
    }


}
