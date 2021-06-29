package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.clientmodel.ClientPlayer;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerCliUpdater implements Listener<ClientPlayer> {
    public static final int CUPBOARD_STARTING_ROW = 12;
    public static final int CUPBOARD_STARTING_COLUMN = 62;
    public static final int POINTS_STARTING_ROW = 20;
    public static final int POINTS_STARTING_COLUMN = 37;

    private final Frame frame;
    private ClientPlayer player;

    private GroupBox cupboardBox, pointsBox;
    private TextBox points;

    private List<ShelfCliUpdater> shelfUpdaters;
    int nBase, nLeaders;

    public PlayerCliUpdater(ClientPlayer player, Frame frame) {
        if(frame == null || player == null)
            throw new NullPointerException();

        this.frame = frame;
        this.player = player;

        nBase = 0;
        nLeaders = 0;
        shelfUpdaters = new ArrayList<>();

        player.addListener(this);

        setup(player);
        update(player);
    }

    public void setup(ClientPlayer player) {
        cupboardBox = new GroupBox("cupboard_box", CUPBOARD_STARTING_ROW, CUPBOARD_STARTING_COLUMN, CUPBOARD_STARTING_ROW + 6, CUPBOARD_STARTING_COLUMN + 61,
                "Cupboard", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        points = new TextBox("points", POINTS_STARTING_ROW + 2, POINTS_STARTING_COLUMN + 9,
                "  ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        pointsBox = new GroupBox("points_box", POINTS_STARTING_ROW, POINTS_STARTING_COLUMN, POINTS_STARTING_ROW + 4, POINTS_STARTING_COLUMN + 20,
                "Points", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK,
                Collections.singletonList(points));

        cupboardBox.setZIndex(-1);

        frame.addElement(cupboardBox);
        frame.addElement(pointsBox);
    }

    @Override
    public void update(ClientPlayer player) {
        int i;

        for(i = nBase; i < player.getCupboard().size(); i++)
            shelfUpdaters.add(new ShelfCliUpdater(player.getCupboard().get(i), frame, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN));

        nBase = i;

        for(i = nLeaders; i < player.getLeaderShelves().size(); i++)
            shelfUpdaters.add(new ShelfCliUpdater(player.getLeaderShelves().get(i), frame, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 30));

        nLeaders = i;

        if(player.getVictoryPoints() < 10)
            points.setText(" " + player.getVictoryPoints());
        else
            points.setText(String.valueOf(player.getVictoryPoints()));
    }
}
