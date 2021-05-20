package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.model.ClientFaithPath;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;

public class FaithPathCliUpdater implements Listener<ClientFaithPath> {
    public static final int STARTING_ROW = 1;
    public static final int STARTING_COLUMN = 36;

    private final Frame frame;
    private ClientFaithPath faithPath;

    private FaithPath faithPathElement;

    public FaithPathCliUpdater(ClientFaithPath faithPath, Frame frame) {
        if(frame == null || faithPath == null)
            throw new NullPointerException();

        this.frame = frame;
        this.faithPath = faithPath;

        faithPath.addListener(this);

        setup(faithPath);
        update(faithPath);
    }

    public void setup(ClientFaithPath faithPath) {
        faithPathElement = new FaithPath("faith_path", STARTING_ROW, STARTING_COLUMN, faithPath.getTiles(), faithPath.getFaithGroups());

        frame.addElement(faithPathElement);
    }

    @Override
    public void update(ClientFaithPath clientFaithPath) {
        faithPathElement.setActiveTile(clientFaithPath.getFaithPoints());
    }
}
