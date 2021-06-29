package it.polimi.ingsw.server.model.faithpath;

import it.polimi.ingsw.common.parser.UniqueSerializableObject;
import it.polimi.ingsw.common.parser.raw.RawFaithPathGroup;
import it.polimi.ingsw.server.Logger;

/**
 * A FaithPathGroup is a group of tiles containing one and only one pope check. Every group is able to grant a
 * certain amount of points to every player in its spaces.
 */
public class FaithPathGroup implements UniqueSerializableObject<RawFaithPathGroup> {
    private final int group;
    private final int points;

    /**
     * Creates an immutable Faith Path Group
     * @param group the id of the group. Must be positive
     * @param points the points granted by the group in case of a Pope Check. Must be positive
     * @throws IllegalArgumentException if either group or points are zero or negative
     */
    public FaithPathGroup(int group, int points) {
        if(group <= 0)
            throw new IllegalArgumentException("Group must be positive");

        if(points <= 0)
            throw new IllegalArgumentException("Points must be positive");

        this.group = group;
        this.points = points;
    }

    /**
     * Returns the faith group
     * @return the faith group
     */
    public int getGroup() {
        return group;
    }

    /**
     * Returns the points granted in case of a Pope Check by the group
     * @return the points granted in case of a Pope Check by the group
     */
    public int getPoints() {
        return points;
    }

    @Override
    public String getStringId() {
        return String.format("%03d", group);
    }

    @Override
    public RawFaithPathGroup toRaw() {
        return new RawFaithPathGroup(this);
    }

    @Override
    public String toString() {
        return "FaithPathGroup{" +
                "group=" + group +
                ", points=" + points +
                '}';
    }

    @Override
    public void printDebugInfo() {
        Logger.log(toString());
    }
}
