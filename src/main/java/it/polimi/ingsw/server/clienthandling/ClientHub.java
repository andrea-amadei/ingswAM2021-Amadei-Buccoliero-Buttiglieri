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
 * between username and socket
 */
public class ClientHub {
    private final List<String> usernames;
    private final List<Socket> clientSockets;

    /**
     * Creates a new empty instance
     */
    public ClientHub(){
        usernames = new ArrayList<>();
        clientSockets = new ArrayList<>();
    }

    /**
     * Adds a new client to the hub.
     * @param username the username of the new player
     * @param socket the socket associated with that player
     * @throws NullPointerException if username or socket are null
     * @throws DuplicateUsernameException if there exists another player with the same username in the hub
     */
    public void addClient(String username, Socket socket) throws DuplicateUsernameException{
        if(username == null || socket == null)
            throw new NullPointerException();

        if(usernames.contains(username))
            throw new DuplicateUsernameException("Another player chose the same username \"" + username +"\"");

        usernames.add(username);
        clientSockets.add(socket);
    }

    /**
     * Disconnects the player from the hub.
     * The username remains, but the associated socket is set to null
     * @param username the username of the disconnected player
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if username is not part of the usernames
     */
    public void disconnectClient(String username){
        if(username == null)
            throw new NullPointerException();

        if(!usernames.contains(username))
            throw new NoSuchElementException("No player with the username \"" + username +"\"");

        clientSockets.set(usernames.indexOf(username), null);
    }

    /**
     * Returns the size of the hub.
     * @return the size of the hub.
     */
    public int size(){
        return usernames.size();
    }

    /**
     * Returns true iff the username is in the hub
     * @param username the requested username
     * @return true iff the username is in the hub
     */
    public boolean contains(String username){
        return usernames.contains(username);
    }

    /**
     * Gets the username-socket pair of the client at position index.
     * @param index the index of the client. It is the order in which the client first connected to the server
     * @return the username-socket pair of the client at position index
     * @throws IndexOutOfBoundsException if index < 0 or index > size of lists
     */
    public Pair<String, Socket> getClientByIndex(int index){
        if(index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        return new Pair<>(usernames.get(index), clientSockets.get(index));
    }

    /**
     * Gets the username-socket pair of the client with the specified username
     * @param username the username of the requested client
     * @return the username-socket pair of the client with the specified username
     * @throws NullPointerException if username is null
     * @throws NoSuchElementException if the specified username is not contained in the hub
     */
    public Pair<String, Socket> getClientByName(String username){
        if(username == null)
            throw new NullPointerException();

        if(!usernames.contains(username))
            throw new NoSuchElementException();

        return new Pair<>(username, clientSockets.get(usernames.indexOf(username)));
    }

    /**
     * Reconnects a client to the hub. It substitutes its socket with the new one
     * @param username the username of the client that wants to be reconnected
     * @param socket the new socket of the client
     * @throws NullPointerException if username or socket are null
     * @throws NoSuchElementException if there is not such username in the hub
     * @throws AlreadyConnectedClientException if the client with this username is still connected
     */
    public void reconnectClient(String username, Socket socket) throws AlreadyConnectedClientException {
        if(username == null || socket == null)
            throw new NullPointerException();

        if(!contains(username))
            throw new NoSuchElementException();

        if(getClientByName(username).getSecond() != null)
            throw new AlreadyConnectedClientException("The client with the username \"" + username + "\" is already connected");

        clientSockets.set(usernames.indexOf(username), socket);
    }

}
