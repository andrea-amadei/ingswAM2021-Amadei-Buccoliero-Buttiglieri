package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AddedNegativePointsException;

public class Player {
    private final String username;
    private boolean connected;
    private final int arrivalId;
    private int points;

    //TODO: Add Board
    //private Board board;

    public Player(String username, int arrivalId) {
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

    public void addPoints(int points) {

        if(points < 0)
            throw new AddedNegativePointsException();

        this.points += points;
    }

    public void resetPoints() {
        points = 0;
    }

    //TODO: Add getters for board
}
