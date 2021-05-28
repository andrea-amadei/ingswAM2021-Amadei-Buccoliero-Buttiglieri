package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.model.ClientFaithPath;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaithPathCliUpdater implements Listener<ClientFaithPath> {
    public static final int STARTING_ROW = 1;
    public static final int STARTING_COLUMN = 36;

    private final Frame frame;
    private ClientFaithPath faithPath;

    private FaithPath faithPathElement;
    private Group group;
    private List<FixedTextBox> checks;

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

        group = new Group("pope_checks");
        group.setZIndex(3);

        checks = new ArrayList<>();

        for(int i = 0; i < faithPath.getCheckpointStatus().size(); i++) {
            FixedTextBox textBox = new FixedTextBox("check_" + (i + 1), STARTING_ROW + i * 3 + 2, STARTING_COLUMN + 134,
                    7, " ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.WHITE_BRIGHT);

            textBox.setAlignLeft(true);
            textBox.setZIndex(3);

            checks.add(textBox);
            group.addElement(textBox);
        }

        frame.addElement(faithPathElement);
        frame.addElement(group);
    }

    @Override
    public void update(ClientFaithPath clientFaithPath) {
        faithPathElement.setActiveTile(clientFaithPath.getFaithPoints());

        for(int i = 0; i < faithPath.getCheckpointStatus().size(); i++) {
            if(faithPath.getCheckpointStatus().get(i) == FaithHolder.CheckpointStatus.UNREACHED) {
                checks.get(i).setForegroundColor(ForegroundColor.WHITE_BRIGHT);
                checks.get(i).setText(" ");
                checks.get(i).setBackgroundColorVisible(false);
            }
            else if(faithPath.getCheckpointStatus().get(i) == FaithHolder.CheckpointStatus.ACTIVE) {
                checks.get(i).setForegroundColor(ForegroundColor.BLUE_BRIGHT);
                checks.get(i).setText("CHECKED");
                checks.get(i).setBackgroundColorVisible(true);
            }
            else if(faithPath.getCheckpointStatus().get(i) == FaithHolder.CheckpointStatus.INACTIVE) {
                checks.get(i).setForegroundColor(ForegroundColor.RED);
                checks.get(i).setText("MISSED");
                checks.get(i).setBackgroundColorVisible(true);
            }
        }
    }
}
