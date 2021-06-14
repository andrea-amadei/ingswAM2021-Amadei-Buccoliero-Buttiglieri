package it.polimi.ingsw.model;


public class Player {
    private final String username;
    private boolean connected;
    private final int arrivalId;
    private int points;

    private final Board board;

    /**
     * Creates a new player. This constructor creates a new player with a default Board.
     * It should only be used for tests.
     * @param username the username of the player
     * @param arrivalId the order in which the player logged in the match
     */
    public Player(String username, int arrivalId) {
        this(username, arrivalId, new Board());
    }

    /**
     * Creates a new player. This constructor is used by the builder.
     * @param username the username of the player
     * @param arrivalId the order in which the player logged in the match
     * @param board the board of this player
     */
    public Player(String username, int arrivalId, Board board){
        if(username == null)
            throw new NullPointerException();

        if(arrivalId < 0)
            throw new IllegalArgumentException("Arrival id cannot be negative");

        this.username = username;
        this.arrivalId = arrivalId;

        connected = true;
        points = 0;

        this.board = board;
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
