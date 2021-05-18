package it.polimi.ingsw.model;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.InvalidFaithPathException;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.PopeCheckAction;
import it.polimi.ingsw.model.fsm.InterruptLauncher;
import it.polimi.ingsw.model.fsm.InterruptListener;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;

/**
 * The class Faith Path contains al the information about the faith path for a game
 * and the methods to move players on it
 */
public class FaithPath implements InterruptLauncher {
    private final List<FaithPathGroup> faithGroupList;
    private final List<FaithPathTile> tiles;
    private final int nGroups;
    private InterruptListener listener;
    private boolean isSinglePlayer;
    private int lorenzoFaith;

    //The list of popeCheck tiles already triggered (indicated by their order)
    transient private final List<Integer> alreadyTriggeredPopeCheckOrders;

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
    public FaithPath(List<FaithPathGroup> faithGroupList, List<FaithPathTile> tiles, boolean isSinglePlayer) throws InvalidFaithPathException {
        Map<Integer, Set<Integer>> coordinates = new HashMap<>();
        TreeSet<Integer> order = new TreeSet<>();
        alreadyTriggeredPopeCheckOrders = new ArrayList<>();
        int prev;

        if(tiles == null)
            throw new NullPointerException();

        if(faithGroupList == null)
            throw new NullPointerException();

        if(tiles.size() == 0)
            throw new IllegalArgumentException("List size must be positive");

        // checking every group is unique
        Set<Integer> groups = new HashSet<>();
        for(FaithPathGroup group : faithGroupList) {
            if(groups.contains(group.getGroup()))
                throw new InvalidFaithPathException("Duplicated group id");

            groups.add(group.getGroup());
        }

        Set<Integer> popeChecks = new HashSet<>();
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
                // if the current group is not present
                if (!groups.contains(i.getPopeGroup()))
                    throw new InvalidFaithPathException("Tile " + i.getOrder() + " references a non existing group");

                // else, if the current group is present
                else {
                    // if the current tile is a pope check and the group has already one
                    if (i.isPopeCheck() && popeChecks.contains(i.getPopeGroup()))
                        throw new InvalidFaithPathException("The same group cannot have multiple pope checks");

                    // else, if the current tile is a pope check but the group doesn't have one already, add it
                    else if (i.isPopeCheck())
                        popeChecks.add(i.getPopeGroup());
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
        if(!groups.equals(popeChecks))
           throw new InvalidFaithPathException("A group doesn't contain a pope check");

        this.faithGroupList = faithGroupList;
        //Finally, set the list...
        this.tiles = tiles;

        // and order it by tile order
        this.tiles.sort(Comparator.comparingInt(FaithPathTile::getOrder));

        // count groups
        this.nGroups = groups.size();

        this.listener = null;

        this.isSinglePlayer = isSinglePlayer;

        this.lorenzoFaith = 0;
    }

    public FaithPath(List<FaithPathGroup> faithGroupList, List<FaithPathTile> tiles) throws InvalidFaithPathException{
        this(faithGroupList, tiles, false);
    }

    public void setSinglePlayer(boolean isSinglePlayer){
        this.isSinglePlayer = isSinglePlayer;
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
    public List<PayloadComponent> executeMovement(int amount, Player player) {
        if(player == null)
            throw new NullPointerException();

        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        List<PayloadComponent> payloadComponents = new ArrayList<>();

        int faith = player.getBoard().getFaithHolder().getFaithPoints();

        if(faith + amount > getTotalLength() - 1)
            amount = (getTotalLength() - 1) - faith;

        //for each tile the player passes, we count the amount of points to be added and we collect all triggered
        // popeCheck(s)
        Pair<List<Integer>, Integer> info = getMovementInformation(amount, faith);
        List<Integer> newPopeCheckOrders = info.getFirst();
        int pointsToAdd = info.getSecond();

        player.getBoard().getFaithHolder().addFaithPoints(amount);
        payloadComponents.add(PayloadFactory.addFaith(player.getUsername(), amount));

        player.addPoints(pointsToAdd);
        payloadComponents.add(PayloadFactory.addPoints(player.getUsername(), pointsToAdd));

        if(newPopeCheckOrders.size() > 0)
            launchInterrupt(new PopeCheckAction(newPopeCheckOrders), ActionQueue.Priority.SERVER_ACTION.ordinal());

        return payloadComponents;
    }

    public List<PayloadComponent> executeLorenzoMovement(int amount){
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        List<PayloadComponent> payloadComponents = new ArrayList<>();

        if(isSinglePlayer) {
            int faith = lorenzoFaith;
            if (faith + amount > getTotalLength() - 1)
                amount = (getTotalLength() - 1) - faith;

            //collect the new popeChecks triggered by Lorenzo
            Pair<List<Integer>, Integer> info = getMovementInformation(amount, faith);
            List<Integer> newPopeCheckOrders = info.getFirst();

            //TODO: add payload for Lorenzo's faith
            lorenzoFaith += amount;

            if(newPopeCheckOrders.size() > 0)
                launchInterrupt(new PopeCheckAction(newPopeCheckOrders), ActionQueue.Priority.SERVER_ACTION.ordinal());
        }

        return payloadComponents;
    }

    /**
     * Returns the list of popeChecks triggered in this movement and the number of victory points collected
     * @param amount the amount of steps to do
     * @param initialFaith the initial position in the faithPath
     * @return the pair containing the list of popeChecks triggered in this movement and
     * the number of victory points collected
     */
    private Pair<List<Integer>, Integer> getMovementInformation(int amount, int initialFaith){
        List<Integer> newPopeChecks = new ArrayList<>();
        int pointsToAdd = 0;
        for(int i = initialFaith  + 1; i <= initialFaith + amount; i++) {
            int tilePoints = tiles.get(i).getVictoryPoints();
            if (tilePoints > 0) {
                pointsToAdd += tilePoints;
            }
            if(tiles.get(i).isPopeCheck()) {
                newPopeChecks.add(tiles.get(i).getOrder());
            }
        }

        return new Pair<>(newPopeChecks, pointsToAdd);
    }

    public List<Integer> getAlreadyTriggeredPopeChecks() {
        return new ArrayList<>(alreadyTriggeredPopeCheckOrders);
    }

    public void addTriggeredPopeCheckOrder(int order){
        alreadyTriggeredPopeCheckOrders.add(order);
    }

    public int getLorenzoFaith(){
        return lorenzoFaith;
    }

    public List<FaithPathGroup> getFaithGroupList(){
        return faithGroupList;
    }
    public List<FaithPathTile> getTiles(){
        return tiles;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for(FaithPathTile i : tiles) {
            str.append(i.toString()).append('\n');
        }

        return str.toString();
    }

    /**
     * Sets the listener of the interrupts launched
     * @param listener the listener
     * @throws NullPointerException if listener is null
     */
    @Override
    public void setListener(InterruptListener listener) {
        if(listener == null)
            throw new NullPointerException();

        this.listener = listener;
    }

    /**
     * Removes the listener (set to null)
     */
    @Override
    public void removeListener() {
        this.listener = null;
    }

    /**
     * Launches the interrupt to the listener (if the listener is not null)
     * @param interrupt the interrupt to be launched
     * @throws NullPointerException if the interrupt is null
     */
    @Override
    public void launchInterrupt(Action interrupt, int priority) {
        if(interrupt == null)
            throw new NullPointerException();
        if(this.listener != null)
            this.listener.launchInterrupt(interrupt, priority);
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }
}
