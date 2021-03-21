package it.polimi.ingsw.model;

import java.util.Objects;

public class Player {
    private final String username;
    private boolean connected;
    private final int arrivalId;
    private int points;

    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_USERNAME_LENGTH = 2;

    //TODO: Add Board
    //private Board board;

    public Player(String username, int arrivalId) {
        if(username == null)
            throw new NullPointerException();

        if(username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH)
            throw new IllegalArgumentException("Username length not valid");

        if(arrivalId < 0)
            throw new IllegalArgumentException("Arrival id cannot be negative");

        this.username = username;
        this.arrivalId = arrivalId;

        connected = true;
        points = 0;

        //TODO: Add board init
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return connected;
    }

    public int getArrivalId() {
        return arrivalId;
    }

    public int getPoints() {
        return points;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void addPoints(int amount) {

        if(amount < 0)
            throw new IllegalArgumentException("Cannot add a negative amount of points");

        this.points += amount;
    }

    public void resetPoints() {
        points = 0;
    }

    //TODO: Add getters for board
}
