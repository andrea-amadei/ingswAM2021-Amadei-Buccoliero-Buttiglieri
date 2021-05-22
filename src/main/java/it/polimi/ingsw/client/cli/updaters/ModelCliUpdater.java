package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.framework.elements.GroupBox;
import it.polimi.ingsw.client.cli.framework.elements.MarbleBox;
import it.polimi.ingsw.client.cli.framework.elements.TextBox;
import it.polimi.ingsw.client.model.ClientMarket;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelCliUpdater implements Listener<ClientModel> {
    public static final int STARTING_ROW = 1;
    public static final int STARTING_COLUMN = 2;

    private final Frame frame;
    private ClientModel clientModel;
    private GroupBox groupBox;
    private String spectatedPlayer;

    private TextBox[] names;
    private TextBox shown;

    public ModelCliUpdater(ClientModel clientModel, Frame frame, String spectatedPlayer) {
        if(frame == null || clientModel == null)
            throw new NullPointerException();

        this.frame = frame;
        this.clientModel = clientModel;
        this.spectatedPlayer = spectatedPlayer;

        clientModel.addListener(this);

        setup(clientModel);
        update(clientModel);
    }

    public void setup(ClientModel clientModel) {
        TextBox divider;

        groupBox = new GroupBox("players", STARTING_ROW, STARTING_COLUMN, STARTING_ROW + clientModel.getPlayers().size() * 2,
                STARTING_COLUMN + 37, "Players", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        groupBox.setDoubleBorder(true);

        for(int i = 0; i < clientModel.getPlayers().size() - 1; i++) {
            divider = new TextBox("divider_" + (i + 1), STARTING_ROW + (i + 1) * 2, STARTING_COLUMN,
                    "╠" + "═".repeat(36) + "╣", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            divider.setZIndex(2);

            groupBox.addElement(divider);
        }

        names = new TextBox[clientModel.getPlayers().size()];

        for(int i = 0; i < clientModel.getPlayers().size(); i++) {
            names[i] = new TextBox("name_" + (i + 1), STARTING_ROW + (i + 1) * 2 - 1, STARTING_COLUMN + 4,
                    clientModel.getPlayers().get(i).getUsername(), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            groupBox.addElement(names[i]);
        }

        List<String> nameList = clientModel.getPlayers().stream().map(ClientPlayer::getUsername).collect(Collectors.toList());

        if(nameList.contains(spectatedPlayer)) {
            shown = new TextBox("shown", (nameList.indexOf(spectatedPlayer) + 1) * 2, STARTING_COLUMN + 30,
                    "Shown", ForegroundColor.WHITE, BackgroundColor.BLACK);

            groupBox.addElement(shown);
        }

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientModel clientModel) {
        for(int i = 0; i < clientModel.getPlayers().size(); i++) {
            if(clientModel.getCurrentPlayer().getUsername().equals(clientModel.getPlayers().get(i).getUsername()))
                names[i].setForegroundColor(ForegroundColor.RED_BRIGHT);
            else
                names[i].setForegroundColor(ForegroundColor.WHITE_BRIGHT);
        }
    }
}
