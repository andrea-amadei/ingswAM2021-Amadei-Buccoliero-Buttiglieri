package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidFaithPathException;

import java.util.*;

/**
 * The class Faith Path contains al the information about the faith path for a game
 * and the methods to move players on it
 */
public class FaithPath {
    private final List<FaithPathTile> tiles;
    private final int nGroups;

    /**
     * Creates a new Faith Path, made of single Faith Path Tiles. The faith path must meet the following criteria:
     * <ul>
     *     <li>All tile must be present, from tile order 0 to tile order n once (where n is the length of the path)</li>
     *     <li>Tiles cannot share the same coordinates</li>
     *     <li>Any pope group must have one and only one pope check</li>
     * </ul>
     * @param tiles a list of all the tiles of the Faith Path
     * @throws NullPointerException if tiles is null
     * @throws IllegalArgumentException if the number of tiles is zero
     * @throws InvalidFaithPathException if:
     * <ul>
     *     <li>Two tiles share the same coordinates</li>
     *     <li>Two tiles have the same order</li>
     *     <li>Any tile is missing for the order</li>
     *     <li>The same group has multiple Pope Checks</li>
     *     <li>Any groups doesn't have a pope check</li>
     * </ul>
     */
    public FaithPath(List<FaithPathTile> tiles) throws InvalidFaithPathException {
        Map<Integer, Set<Integer>> coordinates = new HashMap<>();
        TreeSet<Integer> order = new TreeSet<>();
        Map<Integer, Boolean> group = new HashMap<>();
        int prev;

        if(tiles == null)
            throw new NullPointerException();

        if(tiles.size() == 0)
            throw new IllegalArgumentException("List size must be positive");

        for(FaithPathTile i : tiles) {
            // CHECK FOR DUPLICATE TILES
            // if the X coordinate is already in the structure, add the Y
            if (coordinates.containsKey(i.getX())) {

                // if the Y coordinate is already in the structure, the point was already added
                if (!coordinates.get(i.getX()).add(i.getY()))
                    throw new InvalidFaithPathException("Different tiles cannot share the same coordinates");

                // if not, create a new set and add it
            } else {
                coordinates.put(i.getX(), new HashSet<>() {{
                    add(i.getY());
                }});
            }

            // CHECK FOR CORRECT ORDER
            if (!order.add(i.getOrder()))
                throw new InvalidFaithPathException("Invalid tile order: tile " + i.getOrder() + " is duplicated");

            // CHECK FOR CORRECT GROUP
            // if the current tile is part of a group
            if(i.getPopeGroup() != 0) {
                // if the current group is not already saved
                if (!group.containsKey(i.getPopeGroup())) {
                    group.put(i.getPopeGroup(), i.isPopeCheck());
                }

                // else, if the current group is already saved
                else {
                    // if the current tile is a pope check and the group has already one
                    if (i.isPopeCheck() && group.get(i.getPopeGroup()))
                        throw new InvalidFaithPathException("The same group cannot have multiple pope checks");

                        // else, if the current tile is a pope check but the group doesn't have one already, add it
                    else if (i.isPopeCheck())
                        group.put(i.getPopeGroup(), true);
                }
            }
        }

        // CHECK FOR HOLES IN PATH
        prev = -1;
        for(int i : order) {
            if(prev + 1 != i)
                throw new InvalidFaithPathException("Invalid tile order: missing tile " + (prev + 1) + " in faith path");
            else
                prev = i;
        }

        // CHECK FOR POPE GROUPS WITHOUT CHECK
        for(int i : group.keySet())
            if(!group.get(i))
                throw new InvalidFaithPathException("Pope group " + i + " doesn't have a pope check tile");

        //Finally, set the list...
        this.tiles = tiles;

        // and order it by tile order
        this.tiles.sort(Comparator.comparingInt(FaithPathTile::getOrder));

        // count groups
        this.nGroups = group.size();
    }

    /**
     * @return the amount of PopeGroups int the whole path
     */
    public int getTotalGroups() {
        return nGroups;
    }

    /**
     * @return the total length of the path (the number of tiles)
     */
    public int getTotalLength() {
        return tiles.size();
    }

    /**
     * Executes the movement of a given player and of a given amount of tiles
     * @param amount the amount of tiles the movement covers
     * @param player the player moving
     */
    public void executeMovement(int amount, Player player) {
        if(player == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        int faith = player.getBoard().getFaithHolder().getFaithPoints();

        if(faith + amount > getTotalLength() - 1)
            amount = (getTotalLength() - 1) - faith;

        for(int i = faith  + 1; i <= faith + amount; i++)
            if(tiles.get(i).getVictoryPoints() > 0)
                player.addPoints(tiles.get(i).getVictoryPoints());

        player.getBoard().getFaithHolder().addFaithPoints(amount);

        // TODO: In case of pope check throw interrupt
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for(FaithPathTile i : tiles) {
            str.append(i.toString()).append('\n');
        }

        return str.toString();
    }
}
