package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clienthandling.ClientHandler;
import it.polimi.ingsw.server.clienthandling.Match;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

/**
 * The manager of the games in the server. It handles the creation of new Matches and stores them
 */
public class ServerManager {
    private final Set<Match> matches;
    private final List<String> registeredUsernames;
    private final List<ClientHandler> registeredHandlers;

    public ServerManager(){
        matches = new HashSet<>();
        registeredUsernames = new ArrayList<>();
        registeredHandlers = new ArrayList<>();
    }

    public synchronized Set<Match> getMatches(){
        return new HashSet<>(matches);
    }

    public synchronized Match getMatchByName(String name){
        return matches.stream()
                .filter(m -> m.getGameName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Match \" " + name + "\" does not exist in the server"));
    }

    public synchronized boolean alreadyExistentUsername(String username){
        return matches.stream().anyMatch(m -> m.getUsernames().contains(username));
    }

    public synchronized boolean alreadyExistentGameName(String gameName){
        return matches.stream().anyMatch(m -> m.getGameName().equals(gameName));
    }

    public synchronized List<Pair<String, ClientHandler>> getConnectedClients(){
        List<Pair<String, ClientHandler>> result = new ArrayList<>();
        for(int i = 0; i < registeredUsernames.size(); i++){
            if(registeredHandlers.get(i) != null)
                result.add(new Pair<>(registeredUsernames.get(i), registeredHandlers.get(i)));
        }

        return result;
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
    public synchronized void registerUsername(String username, ClientHandler clientHandler){
        if(registeredUsernames.contains(username))
            throw new IllegalArgumentException();

        if(username == null || clientHandler == null)
            throw new NullPointerException("Specified a null username or a null clientHandler");

        registeredUsernames.add(username);
        registeredHandlers.add(clientHandler);
    }


}
