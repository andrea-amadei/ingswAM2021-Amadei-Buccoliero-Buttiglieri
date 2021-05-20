package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerCliUpdater implements Listener<ClientPlayer> {
    public static final int CUPBOARD_STARTING_ROW = 12;
    public static final int CUPBOARD_STARTING_COLUMN = 62;
    public static final int POINTS_STARTING_ROW = 20;
    public static final int POINTS_STARTING_COLUMN = 37;

    private final Frame frame;
    private ClientPlayer player;

    private GroupBox cupboardBox, pointsBox;
    private TextBox points;

    public PlayerCliUpdater(ClientPlayer player, Frame frame) {
        if(frame == null || player == null)
            throw new NullPointerException();

        this.frame = frame;
        this.player = player;

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

        frame.addElement(cupboardBox);
        frame.addElement(pointsBox);
    }

    private ResourceBox getElem(boolean leader, int id, int n, int row, int column, ClientShelf shelf) {
        String resource;
        boolean faded;
        List<String> resources = List.copyOf(shelf.getStorage().getResources().keySet());
        String trueResource;
        String baseName;

        if(resources.size() != 0)
            trueResource = resources.get(0);
        else
            trueResource = "null";

        if(!shelf.getStorage().getResources().containsKey(trueResource) || shelf.getStorage().getResources().get(trueResource) <= n - 1) {
            resource = shelf.getAcceptedType();
            faded = true;
        }
        else {
            resource = trueResource;
            faded = false;
        }

        if(leader)
            baseName = "elem_leader_";
        else
            baseName = "elem_base_";

        ResourceBox elem = new ResourceBox(
                baseName + (id + 1) + "_" + n,
                row,
                column,
                resource
        );

        elem.setFaded(faded);

        return elem;
    }

    private VisibleElement getDivider(boolean leader, int id, int n, int row, int column) {
        String baseName;

        if(leader)
            baseName = "divider_base_";
        else
            baseName = "divider_leader_";

        return new TextBox(
                baseName + (id + 1) + "_" + n,
                row,
                column,
                "──",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK
        );
    }

    @Override
    public void update(ClientPlayer player) {
        List<ResourceBox> resourceBoxes = new ArrayList<>();

        for(String elem : cupboardBox.getAllElementNames())
            cupboardBox.removeElement(elem);

        List<ClientShelf> sortedList = player.getCupboard()
                .stream()
                .sorted(Comparator.comparing(ClientShelf::getSize))
                .collect(Collectors.toList());

        for(int i = 0; i < sortedList.size(); i++) {
            resourceBoxes.clear();

            switch (sortedList.get(i).getSize()) {
                case 1:
                    resourceBoxes.add(getElem(false, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 15, sortedList.get(i)));

                    break;

                case 2:
                    resourceBoxes.add(getElem(false, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 13, sortedList.get(i)));
                    resourceBoxes.add(getElem(false, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 17, sortedList.get(i)));

                    cupboardBox.addElement(getDivider(false, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 15));

                    break;

                case 3:
                    resourceBoxes.add(getElem(false, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 11, sortedList.get(i)));
                    resourceBoxes.add(getElem(false, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 15, sortedList.get(i)));
                    resourceBoxes.add(getElem(false, i, 3, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 19, sortedList.get(i)));

                    cupboardBox.addElement(getDivider(false, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 13));
                    cupboardBox.addElement(getDivider(false, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 17));

                    break;
            }

            cupboardBox.addElement(new FixedTextBox(
                    "name_base_" + (i + 1),
                    CUPBOARD_STARTING_ROW + (i * 2) + 1,
                    CUPBOARD_STARTING_COLUMN + 1,
                    8,
                    sortedList.get(i).getStorage().getId(),
                    ForegroundColor.WHITE_BRIGHT,
                    BackgroundColor.BLACK
            ));

            cupboardBox.addElement(new TextBox(
                    "selected_base_" + (i + 1),
                    CUPBOARD_STARTING_ROW + (i * 2) + 1,
                    CUPBOARD_STARTING_COLUMN + 22,
                    " ",
                    ForegroundColor.WHITE_BRIGHT,
                    BackgroundColor.BLACK
            ));

            for(VisibleElement e : resourceBoxes)
                cupboardBox.addElement(e);
        }

        sortedList = player.getLeaderShelves()
                .stream()
                .sorted(Comparator.comparing(ClientShelf::getSize))
                .collect(Collectors.toList());

        for(int i = 0; i < sortedList.size(); i++) {
            resourceBoxes.clear();

            switch (sortedList.get(i).getSize()) {
                case 1:
                    resourceBoxes.add(getElem(true, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 45, sortedList.get(i)));

                    break;

                case 2:
                    resourceBoxes.add(getElem(true, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 43, sortedList.get(i)));
                    resourceBoxes.add(getElem(true, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 47, sortedList.get(i)));

                    cupboardBox.addElement(getDivider(true, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 45));

                    break;

                case 3:
                    resourceBoxes.add(getElem(true, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 41, sortedList.get(i)));
                    resourceBoxes.add(getElem(true, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 45, sortedList.get(i)));
                    resourceBoxes.add(getElem(true, i, 3, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 49, sortedList.get(i)));

                    cupboardBox.addElement(getDivider(true, i, 1, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 43));
                    cupboardBox.addElement(getDivider(true, i, 2, CUPBOARD_STARTING_ROW + (i * 2) + 1, CUPBOARD_STARTING_COLUMN + 47));

                    break;
            }

            cupboardBox.addElement(new FixedTextBox(
                    "name_leader_" + (i + 1),
                    CUPBOARD_STARTING_ROW + (i * 2) + 1,
                    CUPBOARD_STARTING_COLUMN + 31,
                    8,
                    sortedList.get(i).getStorage().getId(),
                    ForegroundColor.WHITE_BRIGHT,
                    BackgroundColor.BLACK
            ));

            cupboardBox.addElement(new TextBox(
                    "selected_leader_" + (i + 1),
                    CUPBOARD_STARTING_ROW + (i * 2) + 1,
                    CUPBOARD_STARTING_COLUMN + 52,
                    " ",
                    ForegroundColor.WHITE_BRIGHT,
                    BackgroundColor.BLACK
            ));

            for(VisibleElement e : resourceBoxes)
                cupboardBox.addElement(e);
        }

        if(player.getVictoryPoints() < 10)
            points.setText(" " + player.getVictoryPoints());
        else
            points.setText(String.valueOf(player.getVictoryPoints()));
    }
}
