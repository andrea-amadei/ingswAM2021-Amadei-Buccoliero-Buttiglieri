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
        return registeredUsernames.stream().anyMatch(u -> u.equals(username));
    }

    public synchronized boolean alreadyExistentGameName(String gameName){
        return matches.stream().anyMatch(m -> m.getGameName().equals(gameName));
    }

    public synchronized boolean isPlayerConnected(String username){
        if(username == null)
            return false;
        int index = registeredUsernames.indexOf(username);
        if(index == -1)
            return false;

        return registeredHandlers.get(index) != null;
    }

    public synchronized List<Pair<String, ClientHandler>> getConnectedClients(){
        List<Pair<String, ClientHandler>> result = new ArrayList<>();
        for(int i = 0; i < registeredUsernames.size(); i++){
            if(registeredHandlers.get(i) != null)
                result.add(new Pair<>(registeredUsernames.get(i), registeredHandlers.get(i)));
        }

        return result;
    }


    /**
     * Adds a new match
     * @param match the new match
     * @throws NullPointerException if match is null
     * @throws IllegalArgumentException if there is another match with the same name or if a player of the new match has
     */
    public synchronized void addMatch(Match match){
        if(match == null)
            throw new NullPointerException();
        if(alreadyExistentGameName(match.getGameName()))
            throw new IllegalArgumentException("There is another match with name \"" + match.getGameName() + "\"");
        matches.add(match);
    }

    /**
     * Removes the specified match from the set of matches
     * @param match the match to be removed
     * @throws NullPointerException if match is null
     */
    public synchronized void removeMatch(Match match){
        if(match == null)
            throw new NullPointerException();
        matches.remove(match);
    }

    /**
     * Registers a new username
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

    /**
     * Sets the clientHandler associated with the specified username to null
     * @param username the username to disconnect from the server
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if username is not present in the server
     */
    public synchronized void disconnectPlayer(String username){
        if(username == null)
            throw new NullPointerException("Username is null");
        if(!registeredUsernames.contains(username))
            throw new NoSuchElementException("The user is not in the server");

        registeredHandlers.set(registeredUsernames.indexOf(username), null);
    }

    public synchronized void reconnectPlayer(String username, ClientHandler handler){
        if(username == null)
            throw new NullPointerException("Username is null");
        if(!registeredUsernames.contains(username))
            throw new NoSuchElementException("There is no \"" + username + "\" in the server");

        registeredHandlers.set(registeredUsernames.indexOf(username), handler);
    }


}
