package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.model.ClientFlagHolder;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FlagHolderCliUpdater implements Listener<ClientFlagHolder> {
    public static final int STARTING_ROW = 12;
    public static final int STARTING_COLUMN = 2;

    private final Frame frame;
    private ClientFlagHolder flagHolder;

    private Map<FlagColor, Map<Integer, FlagBoxWithAmount>> flagBoxes;
    private GroupBox groupBox;

    public FlagHolderCliUpdater(ClientFlagHolder flagHolder, Frame frame) {
        if(frame == null || flagHolder == null)
            throw new NullPointerException();

        this.frame = frame;
        this.flagHolder = flagHolder;

        flagHolder.addListener(this);

        setup(flagHolder);
        update(flagHolder);
    }

    public void setup(ClientFlagHolder flagHolder) {
        flagBoxes = new HashMap<>();

        flagBoxes.put(FlagColor.GREEN, new HashMap<>() {{
            put(1, new FlagBoxWithAmount("flag_green_1", STARTING_ROW + 1, STARTING_COLUMN + 1, 1, "green", 0));
            put(2, new FlagBoxWithAmount("flag_green_2", STARTING_ROW + 5, STARTING_COLUMN + 1, 2, "green", 0));
            put(3, new FlagBoxWithAmount("flag_green_3", STARTING_ROW + 9, STARTING_COLUMN + 1, 3, "green", 0));
        }});

        flagBoxes.put(FlagColor.BLUE, new HashMap<>() {{
            put(1, new FlagBoxWithAmount("flag_blue_1", STARTING_ROW + 1, STARTING_COLUMN + 9, 1, "blue", 0));
            put(2, new FlagBoxWithAmount("flag_blue_2", STARTING_ROW + 5, STARTING_COLUMN + 9, 2, "blue", 0));
            put(3, new FlagBoxWithAmount("flag_blue_3", STARTING_ROW + 9, STARTING_COLUMN + 9, 3, "blue", 0));
        }});

        flagBoxes.put(FlagColor.YELLOW, new HashMap<>() {{
            put(1, new FlagBoxWithAmount("flag_yellow_1", STARTING_ROW + 1, STARTING_COLUMN + 17, 1, "yellow", 0));
            put(2, new FlagBoxWithAmount("flag_yellow_2", STARTING_ROW + 5, STARTING_COLUMN + 17, 2, "yellow", 0));
            put(3, new FlagBoxWithAmount("flag_yellow_3", STARTING_ROW + 9, STARTING_COLUMN + 17, 3, "yellow", 0));
        }});

        flagBoxes.put(FlagColor.PURPLE, new HashMap<>() {{
            put(1, new FlagBoxWithAmount("flag_purple_1", STARTING_ROW + 1, STARTING_COLUMN + 25, 1, "purple", 0));
            put(2, new FlagBoxWithAmount("flag_purple_2", STARTING_ROW + 5, STARTING_COLUMN + 25, 2, "purple", 0));
            put(3, new FlagBoxWithAmount("flag_purple_3", STARTING_ROW + 9, STARTING_COLUMN + 25, 3, "purple", 0));
        }});

        groupBox = new GroupBox("flag_holder", STARTING_ROW, STARTING_COLUMN, STARTING_ROW + 12, STARTING_COLUMN + 32,
                "Flags - Total: 0",
                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        for(FlagColor color : flagBoxes.keySet())
            for(Integer level : flagBoxes.get(color).keySet())
                groupBox.addElement(flagBoxes.get(color).get(level));

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientFlagHolder clientFlagHolder) {
        for(FlagColor color : flagBoxes.keySet())
            for(Integer level : flagBoxes.get(color).keySet())
                flagBoxes.get(color).get(level).setAmount(0);

        for(RawLevelFlag i : clientFlagHolder.getFlags())
            flagBoxes.get(i.getColor()).get(i.getLevel()).setAmount(flagBoxes.get(i.getColor()).get(i.getLevel()).getAmount() + 1);

        groupBox.setText("Flags - Total: " + clientFlagHolder.getFlags().size());
    }
}
