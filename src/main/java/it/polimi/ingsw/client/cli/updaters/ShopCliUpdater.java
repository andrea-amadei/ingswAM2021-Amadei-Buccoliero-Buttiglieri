package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.clientmodel.ClientShop;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.Map;

public class ShopCliUpdater implements Listener<ClientShop> {
    public static final int STARTING_ROW = 1;
    public static final int STARTING_COLUMN = 91;

    private final Frame frame;
    private ClientShop clientShop;

    private GroupBox[][] cards;
    private Group group;

    public ShopCliUpdater(ClientShop clientShop, Frame frame) {
        if(frame == null || clientShop == null)
            throw new NullPointerException();

        this.frame = frame;
        this.clientShop = clientShop;

        clientShop.addListener(this);

        setup(clientShop);
        update(clientShop);
    }

    public void setup(ClientShop clientShop) {
        cards = new GroupBox[clientShop.getRowSize()][clientShop.getColSize()];
        group = new Group("shop");

        for(int i = 0; i < clientShop.getRowSize(); i++)
            for(int j = 0; j < clientShop.getColSize(); j++) {
                cards[i][j] = new GroupBox("card_" + (i + 1) + "_" + (j + 1), STARTING_ROW + 15 * i, STARTING_COLUMN + j * 22,
                        STARTING_ROW + 15 * i + 14, STARTING_COLUMN + j * 22 + 20, "Card " + (i + 1) + " - " + (j + 1),
                        ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

                group.addElement(cards[i][j]);
            }

        frame.addElement(group);
    }

    @Override
    public void update(ClientShop clientShop) {
        GroupBox crafting;
        Map<String, Integer> resources;
        BackgroundColor bg;
        ForegroundColor fg;
        int k;

        for(int i = 0; i < clientShop.getRowSize(); i++)
            for(int j = 0; j < clientShop.getColSize(); j++) {

                for(VisibleElement elem : cards[i][j].getAllElements())
                    cards[i][j].removeElement(elem.getName());

                cards[i][j].setDoubleBorder(true);

                if(clientShop.getSelectedCardRow() != null && clientShop.getSelectedCardCol() != null && clientShop.getSelectedCardRow() == i && clientShop.getSelectedCardCol() == j) {
                    bg = BackgroundColor.GREEN;
                    fg = ForegroundColor.GREEN;
                }
                else {
                    bg = BackgroundColor.BLACK;
                    fg = ForegroundColor.BLACK;
                }

                cards[i][j].setBackgroundColor(bg);

                if(clientShop.getGrid()[i][j] == null) {
                    cards[i][j].addElement(
                            new TextBox("empty", cards[i][j].getStartingRow() + 7, cards[i][j].getStartingColumn() + 8,
                                    "EMPTY", ForegroundColor.WHITE_BRIGHT, bg)
                    );
                }
                else {
                    resources = clientShop.getGrid()[i][j].getCost();
                    k = 0;
                    for (String res : resources.keySet()) {
                        cards[i][j].addElement(
                                new ResourceBoxWithAmount("cost_" + res,
                                        cards[i][j].getStartingRow() + 2 + k, cards[i][j].getStartingColumn() + 1,
                                        res, resources.get(res), ForegroundColor.WHITE_BRIGHT, bg)
                        );

                        k++;
                    }

                    cards[i][j].addElement(
                            new FlagBox("flag", cards[i][j].getStartingRow() + 1, cards[i][j].getStartingColumn() + 16,
                                    clientShop.getGrid()[i][j].getLevel(), clientShop.getGrid()[i][j].getFlag().name())
                    );

                    ((FlagBox) cards[i][j].getElement("flag")).setBlockCharactersColor(fg);

                    cards[i][j].addElement(
                            new TextBox("cost", cards[i][j].getStartingRow() + 1, cards[i][j].getStartingColumn() + 1,
                                    "Cost:", ForegroundColor.WHITE_BRIGHT, bg)
                    );

                    cards[i][j].addElement(
                            new TextBox("points", cards[i][j].getStartingRow() + 4, cards[i][j].getStartingColumn() + 10,
                                    "Points: " + clientShop.getGrid()[i][j].getPoints(),
                                    ForegroundColor.WHITE_BRIGHT, bg)
                    );

                    cards[i][j].addElement(
                            new TextBox("id", cards[i][j].getStartingRow() + 5, cards[i][j].getStartingColumn() + 14,
                                    "Id: " + clientShop.getGrid()[i][j].getId(),
                                    ForegroundColor.WHITE_BRIGHT, bg)
                    );

                    crafting = new GroupBox("crafting", cards[i][j].getStartingRow() + 6, cards[i][j].getStartingColumn() + 1,
                            cards[i][j].getStartingRow() + 13, cards[i][j].getStartingColumn() + 19, "Crafting",
                            ForegroundColor.WHITE_BRIGHT, bg);

                    crafting.addElement(
                            new TextBox("arrow", cards[i][j].getStartingRow() + 8, cards[i][j].getStartingColumn() + 9,
                                    "->", ForegroundColor.WHITE_BRIGHT, bg)
                    );

                    crafting.addElement(
                            new TextBox("level", cards[i][j].getStartingRow() + 7, cards[i][j].getStartingColumn() + 2,
                                    "Level: " + clientShop.getGrid()[i][j].getLevel(), ForegroundColor.WHITE_BRIGHT, bg)
                    );

                    resources = clientShop.getGrid()[i][j].getCrafting().getInput();
                    k = 0;
                    for (String res : resources.keySet()) {
                        crafting.addElement(
                                new ResourceBoxWithAmount("input_" + res,
                                        cards[i][j].getStartingRow() + 8 + k, cards[i][j].getStartingColumn() + 2,
                                        res, resources.get(res), ForegroundColor.WHITE_BRIGHT, bg)
                        );

                        k++;
                    }

                    resources = clientShop.getGrid()[i][j].getCrafting().getOutput();
                    k = 0;
                    for (String res : resources.keySet()) {
                        crafting.addElement(
                                new ResourceBoxWithAmount("output_" + res,
                                        cards[i][j].getStartingRow() + 8 + k, cards[i][j].getStartingColumn() + 13,
                                        res, resources.get(res), ForegroundColor.WHITE_BRIGHT, bg)
                        );

                        k++;
                    }

                    if(clientShop.getGrid()[i][j].getCrafting().getFaithOutput() > 0) {
                        crafting.addElement(
                                new TextBox("plus", cards[i][j].getStartingRow() + 12, cards[i][j].getStartingColumn() + 10,
                                        "+", ForegroundColor.WHITE_BRIGHT, bg)
                        );

                        crafting.addElement(
                                new ResourceBoxWithAmount("output_faith",
                                        cards[i][j].getStartingRow() + 12, cards[i][j].getStartingColumn() + 13,
                                        "faith", clientShop.getGrid()[i][j].getCrafting().getFaithOutput(),
                                        ForegroundColor.WHITE_BRIGHT, bg)
                        );
                    }

                    cards[i][j].addElement(crafting);
                }
            }
    }
}
