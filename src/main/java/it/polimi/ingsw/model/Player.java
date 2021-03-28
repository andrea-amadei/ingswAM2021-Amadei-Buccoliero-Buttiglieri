package it.polimi.ingsw.model;


public class Player {
    private final String username;
    private boolean connected;
    private final int arrivalId;
    private int points;

    private Board board;

    public Player(String username, int arrivalId) {
        if(username == null)
            throw new NullPointerException();

        if(username.length() < GameParameters.MIN_USERNAME_LENGTH || username.length() > GameParameters.MAX_USERNAME_LENGTH)
            throw new IllegalArgumentException("Username length not valid");

        if(arrivalId < 0)
            throw new IllegalArgumentException("Arrival id cannot be negative");

        this.username = username;
        this.arrivalId = arrivalId;

        connected = true;
        points = 0;

        this.board = new Board();
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

    public Board getBoard(){return board;}
}
