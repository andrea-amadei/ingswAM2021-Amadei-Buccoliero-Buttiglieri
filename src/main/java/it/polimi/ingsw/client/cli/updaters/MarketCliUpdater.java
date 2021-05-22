package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.client.model.ClientMarket;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class MarketCliUpdater implements Listener<ClientMarket> {
    public static final int STARTING_ROW = 17;
    public static final int STARTING_COLUMN = 2;

    private final Frame frame;
    private ClientMarket market;
    private GroupBox groupBox;

    private MarbleBox[][] marbles;
    private MarbleBox odd;
    private TextBox[] labels;
    private TextBox label_odd;
    private TextBox[] arrows;

    public MarketCliUpdater(ClientMarket market, Frame frame) {
        if(frame == null || market == null)
            throw new NullPointerException();

        this.frame = frame;
        this.market = market;

        market.addListener(this);

        setup(market);
        update(market);
    }

    public void setup(ClientMarket market) {
        int i, j;

        groupBox = new GroupBox("market", STARTING_ROW, STARTING_COLUMN, STARTING_ROW + market.getRowSize() * 4 + 2 + 10,
                STARTING_COLUMN + market.getColSize() * 7 + 31 + 6, "Market",
                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        groupBox.setDoubleBorder(true);

        marbles = new MarbleBox[market.getColSize()][market.getRowSize()];
        labels = new TextBox[market.getColSize() + market.getRowSize()];
        arrows = new TextBox[market.getColSize() + market.getRowSize()];

        for(i = 0; i < market.getColSize(); i++)
            for(j = 0; j < market.getRowSize(); j++) {
                marbles[i][j] = new MarbleBox("marble_" + (i + 1) + "_" + (j + 1), STARTING_ROW + j * 4 + 3, STARTING_COLUMN + i * 9 + 7,
                        market.getMarket().getMarbles().get(j * market.getColSize() + i));

                groupBox.addElement(marbles[i][j]);
            }

        odd = new MarbleBox("marble_odd", STARTING_ROW + 3, STARTING_COLUMN + market.getRowSize() * 9 + 25,
                market.getMarket().getOdd());
        groupBox.addElement(odd);

        for(i = 0; i < market.getRowSize(); i++) {
            labels[i] = new TextBox("label_col_" + (i + 1), STARTING_ROW + i * 4 + 4, STARTING_COLUMN + 3,
                    String.valueOf(i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            groupBox.addElement(labels[i]);
        }

        for(i = 0; i < market.getColSize(); i++) {
            labels[market.getRowSize() + i] = new TextBox("label_row_" + (i + 1), STARTING_ROW + 1, STARTING_COLUMN + i * 9 + 10,
                    String.valueOf(i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            groupBox.addElement(labels[market.getRowSize() + i]);
        }

        label_odd = new TextBox("label_odd", STARTING_ROW + 7, STARTING_COLUMN + market.getRowSize() * 9 + 27,
                "Odd", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        groupBox.addElement(label_odd);

        for(i = 0; i < market.getRowSize(); i++) {
            arrows[i] = new TextBox("arrow_col_" + (i + 1), STARTING_ROW + i * 4 + 4, STARTING_COLUMN + market.getRowSize() * 9 + 16,
                    "<", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            groupBox.addElement(arrows[i]);
        }

        for(i = 0; i < market.getColSize(); i++) {
            arrows[market.getRowSize() + i] = new TextBox("arrow_bottom_row_" + (i + 1), STARTING_ROW + market.getRowSize() * 4 + 3, STARTING_COLUMN + i * 9 + 10,
                    "^", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            groupBox.addElement(arrows[market.getRowSize() + i]);
        }

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientMarket market) {
        for(int i = 0; i < market.getColSize(); i++)
            for(int j = 0; j < market.getRowSize(); j++)
                marbles[i][j].setMarble(market.getMarket().getMarbles().get(j * market.getColSize() + i));

        odd.setMarble(market.getMarket().getOdd());
    }
}
