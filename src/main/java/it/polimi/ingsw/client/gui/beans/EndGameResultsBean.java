package it.polimi.ingsw.client.gui.beans;

import java.util.List;

public class EndGameResultsBean {
    private boolean hasLorenzoWon;
    private boolean isGameCrashed;
    private List<String> usernames;
    private List<Integer> points;

    public boolean isHasLorenzoWon() {
        return hasLorenzoWon;
    }

    public void setHasLorenzoWon(boolean hasLorenzoWon) {
        this.hasLorenzoWon = hasLorenzoWon;
    }

    public boolean isGameCrashed() {
        return isGameCrashed;
    }

    public void setGameCrashed(boolean gameCrashed) {
        isGameCrashed = gameCrashed;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }
}
