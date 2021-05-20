package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientBaseStorage;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;

public class BaseStorageCliUpdater implements Listener<ClientBaseStorage> {
    private final int startingRow;
    private final int startingColumn;
    private final boolean hideIfEmpty;
    private final String title;

    private final Frame frame;
    private ClientBaseStorage clientBaseStorage;

    private GroupBox groupBox;
    private ResourceBoxWithAmount gold, stone, servant, shield;
    private TextBox txt_gold, txt_stone, txt_servant, txt_shield;

    public BaseStorageCliUpdater(ClientBaseStorage clientBaseStorage, Frame frame, int startingRow, int startingColumn, String title, boolean hideIfEmpty) {
        if(frame == null || clientBaseStorage == null || title == null)
            throw new NullPointerException();

        this.frame = frame;
        this.clientBaseStorage = clientBaseStorage;
        this.startingRow = startingRow;
        this.startingColumn = startingColumn;
        this.title = title;
        this.hideIfEmpty = hideIfEmpty;

        clientBaseStorage.addListener(this);

        setup(clientBaseStorage);
        update(clientBaseStorage);
    }

    public void setup(ClientBaseStorage clientBaseStorage) {
        gold = new ResourceBoxWithAmount(title.toLowerCase() + "_gold", startingRow + 1, startingColumn + 1, "gold", 0);
        stone = new ResourceBoxWithAmount(title.toLowerCase() + "_stone", startingRow + 2, startingColumn + 1, "stone", 0);
        servant = new ResourceBoxWithAmount(title.toLowerCase() + "_servant", startingRow + 3, startingColumn + 1, "servant", 0);
        shield = new ResourceBoxWithAmount(title.toLowerCase() + "_shield", startingRow + 4, startingColumn + 1, "shield", 0);

        txt_gold = new TextBox(title.toLowerCase() + "_gold_sel", startingRow + 1, startingColumn + 9, " ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        txt_stone = new TextBox(title.toLowerCase() + "_stone_sel", startingRow + 2, startingColumn + 9, " ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        txt_servant = new TextBox(title.toLowerCase() + "_servant_sel", startingRow + 3, startingColumn + 9, " ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        txt_shield = new TextBox(title.toLowerCase() + "_shield_sel", startingRow + 4, startingColumn + 9, " ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        groupBox = new GroupBox(title.toLowerCase() + "_box", startingRow, startingColumn, startingRow + 5, startingColumn + 15,
                title, ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK,
                Arrays.asList(gold, stone, servant, shield, txt_gold, txt_stone, txt_servant, txt_shield));

        if(hideIfEmpty)
            groupBox.setVisible(false);

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientBaseStorage clientBaseStorage) {
        if(clientBaseStorage.getStorage().getResources().containsKey("gold"))
            gold.setAmount(clientBaseStorage.getStorage().getResources().get("gold"));

        if(clientBaseStorage.getStorage().getResources().containsKey("stone"))
            stone.setAmount(clientBaseStorage.getStorage().getResources().get("stone"));

        if(clientBaseStorage.getStorage().getResources().containsKey("servant"))
            servant.setAmount(clientBaseStorage.getStorage().getResources().get("servant"));

        if(clientBaseStorage.getStorage().getResources().containsKey("shield"))
            shield.setAmount(clientBaseStorage.getStorage().getResources().get("shield"));

        if(clientBaseStorage.getSelectedResources().containsKey("gold") && clientBaseStorage.getSelectedResources().get("gold") > 0) {
            txt_gold.setText("Sel: " + clientBaseStorage.getSelectedResources().get("gold"));
            txt_gold.setBackgroundColor(BackgroundColor.GREEN);
            gold.setBackgroundColor(BackgroundColor.GREEN);
        }
        else {
            txt_gold.setText(" ");
            txt_gold.setBackgroundColor(BackgroundColor.BLACK);
            gold.setBackgroundColor(BackgroundColor.BLACK);
        }

        if(clientBaseStorage.getSelectedResources().containsKey("stone") && clientBaseStorage.getSelectedResources().get("stone") > 0) {
            txt_stone.setText("Sel: " + clientBaseStorage.getSelectedResources().get("stone"));
            txt_stone.setBackgroundColor(BackgroundColor.GREEN);
            stone.setBackgroundColor(BackgroundColor.GREEN);
        }
        else {
            txt_stone.setText(" ");
            txt_stone.setBackgroundColor(BackgroundColor.BLACK);
            stone.setBackgroundColor(BackgroundColor.BLACK);
        }

        if(clientBaseStorage.getSelectedResources().containsKey("servant") && clientBaseStorage.getSelectedResources().get("servant") > 0) {
            txt_servant.setText("Sel: " + clientBaseStorage.getSelectedResources().get("servant"));
            txt_servant.setBackgroundColor(BackgroundColor.GREEN);
            servant.setBackgroundColor(BackgroundColor.GREEN);
        }
        else {
            txt_servant.setText(" ");
            txt_servant.setBackgroundColor(BackgroundColor.BLACK);
            servant.setBackgroundColor(BackgroundColor.BLACK);
        }

        if(clientBaseStorage.getSelectedResources().containsKey("shield") && clientBaseStorage.getSelectedResources().get("shield") > 0) {
            txt_shield.setText("Sel: " + clientBaseStorage.getSelectedResources().get("shield"));
            txt_shield.setBackgroundColor(BackgroundColor.GREEN);
            shield.setBackgroundColor(BackgroundColor.GREEN);
        }
        else {
            txt_shield.setText(" ");
            txt_shield.setBackgroundColor(BackgroundColor.BLACK);
            shield.setBackgroundColor(BackgroundColor.BLACK);
        }

        if(hideIfEmpty)
            groupBox.setVisible(gold.getAmount() != 0 || stone.getAmount() != 0 || servant.getAmount() != 0 || shield.getAmount() != 0);
    }
}
