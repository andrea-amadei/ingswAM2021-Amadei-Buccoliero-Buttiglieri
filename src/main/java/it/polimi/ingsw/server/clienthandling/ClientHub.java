package it.polimi.ingsw.server.clienthandling;

import it.polimi.ingsw.exceptions.AlreadyConnectedClientException;
import it.polimi.ingsw.exceptions.DuplicateUsernameException;
import it.polimi.ingsw.utils.Pair;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class manages the access to the clients.
 * In particular it handles the client arrival order and it stores the association
 * between username and clientHandler
 */
public class ClientHub {
    private final List<String> usernames;
    private final List<ClientHandler> clientHandlers;

    /**
     * Creates a new empty instance
     */
    public ClientHub(){
        usernames = new ArrayList<>();
        clientHandlers = new ArrayList<>();
    }

    /**
     * Adds a new client to the hub.
     * @param username the username of the new player
     * @param clientHandler the client handler associated with that player
     * @throws NullPointerException if username or socket are null
     * @throws IllegalArgumentException if there exists another player with the same username in the hub
     */
    public synchronized void addClient(String username, ClientHandler clientHandler){
        if(username == null || clientHandler == null)
            throw new NullPointerException();

        if(usernames.contains(username))
            throw new IllegalArgumentException("Another player chose the same username \"" + username +"\"");

        usernames.add(username);
        clientHandlers.add(clientHandler);
    }

    /**
     * Disconnects the player from the hub.
     * The username remains, but the associated socket is set to null
     * @param username the username of the disconnected player
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if username is not part of the usernames
     */
    public synchronized void disconnectClient(String username){
        if(username == null)
            throw new NullPointerException();

        if(!usernames.contains(username))
            throw new NoSuchElementException("No player with the username \"" + username +"\"");

        clientHandlers.set(usernames.indexOf(username), null);
    }

    /**
     * Returns the size of the hub.
     * @return the size of the hub.
     */
    public synchronized int size(){
        return usernames.size();
    }

    /**
     * Returns true iff the username is in the hub
     * @param username the requested username
     * @return true iff the username is in the hub
     */
    public synchronized boolean contains(String username){
        return usernames.contains(username);
    }

    /**
     * Gets the username-clientHandler pair of the client at position index.
     * @param index the index of the client. It is the order in which the client first connected to the server
     * @return the username-clientHandler pair of the client at position index
     * @throws IndexOutOfBoundsException if index < 0 or index > size of lists
     */
    public synchronized Pair<String, ClientHandler> getClientByIndex(int index){
        if(index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        return new Pair<>(usernames.get(index), clientHandlers.get(index));
    }

    /**
     * Gets the username-clientHandler pair of the client with the specified username
     * @param username the username of the requested client
     * @return the username-clientHandler pair of the client with the specified username
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if the specified username is not contained in the hub
     */
    public synchronized Pair<String, ClientHandler> getClientByName(String username){
        if(username == null)
            throw new NullPointerException();

        if(!usernames.contains(username))
            throw new NoSuchElementException();

        return new Pair<>(username, clientHandlers.get(usernames.indexOf(username)));
    }

    /**
     * Return the list of usernames sorted by arrival order
     * @return the list of usernames sorted by arrival order
     */
    public synchronized List<String> getUsernames(){
        return usernames;
    }

    /**
     * Reconnects a client to the hub. It substitutes its socket with the new one
     * @param username the username of the client that wants to be reconnected
     * @param clientHandler the new clientHandler of the client
     * @throws NullPointerException if username or socket are null
     * @throws NoSuchElementException if there is not such username in the hub
     * @throws AlreadyConnectedClientException if the client with this username is still connected
     */
    public synchronized void reconnectClient(String username, ClientHandler clientHandler) throws AlreadyConnectedClientException {
        if(username == null || clientHandler == null)
            throw new NullPointerException();

        if(!contains(username))
            throw new NoSuchElementException();

        if(getClientByName(username).getSecond() != null)
            throw new AlreadyConnectedClientException("The client with the username \"" + username + "\" is already connected");

        clientHandlers.set(usernames.indexOf(username), clientHandler);
    }

}
