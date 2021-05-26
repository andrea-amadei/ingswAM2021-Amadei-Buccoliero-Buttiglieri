package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.client.model.ClientMarket;
import it.polimi.ingsw.client.model.ConversionOption;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MarketCliUpdater implements Listener<ClientMarket> {
    public static final int STARTING_ROW = 16;
    public static final int STARTING_COLUMN = 2;

    private final Frame frame;
    private ClientMarket market;
    private GroupBox groupBox;

    private MarbleBox[][] marbles;
    private MarbleBox odd;
    private TextBox[] labels;
    private TextBox label_odd;
    private TextBox[] arrows;

    private MarbleBox[] conversions;

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

        groupBox = new GroupBox("market", STARTING_ROW, STARTING_COLUMN, STARTING_ROW + 29,
                STARTING_COLUMN + 78, "Market",
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

        groupBox.addElement(
                new TextBox("divider", STARTING_ROW + 16, STARTING_COLUMN,
                        "╠═ Conversions " + "═".repeat(63) + "╣",
                        ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
        );

        int nConversions = Math.max(market.getColSize(), market.getRowSize());
        conversions = new MarbleBox[nConversions];

        for(i = 0; i < nConversions; i++) {
            conversions[i] = new MarbleBox("marble_conversion_" + (i + 1), STARTING_ROW + 18, STARTING_COLUMN + i * 18 + 7,
                    MarbleColor.WHITE);
            conversions[i].setVisible(true);

            groupBox.addElement(conversions[i]);
        }

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientMarket market) {
        int i, j, k;

        for(i = 0; i < market.getColSize(); i++)
            for(j = 0; j < market.getRowSize(); j++)
                marbles[i][j].setMarble(market.getMarket().getMarbles().get(j * market.getColSize() + i));

        odd.setMarble(market.getMarket().getOdd());

        for(VisibleElement elem : groupBox.getAllElements())
            if(elem.getName().startsWith("conversion_"))
                groupBox.removeElement(elem.getName());

        int maxOptions = market.getPossibleConversions()
                .stream()
                .max(Comparator.comparing(List::size))
                .orElse(new ArrayList<>())
                .size();

        for(i = 0; i < maxOptions; i++) {
            groupBox.addElement(
                    new TextBox("conversion_label" + (i + 1), STARTING_ROW + i * 2 + 22, STARTING_COLUMN + 3,
                            String.valueOf(i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
            );
        }

        for(i = 0; i < conversions.length; i++)
            if(i < market.getSelectedMarbles().size()) {
                conversions[i].setVisible(true);
                conversions[i].setMarble(market.getSelectedMarbles().get(i));

                for(j = 0; j < market.getPossibleConversions().get(i).size(); j++) {

                    for(k = 0; k < market.getPossibleConversions().get(i).get(j).getResources().size(); k++) {
                        groupBox.addElement(
                                new ResourceBox("conversion_" + (i + 1) + "_" + (j + 1) + "_" + (k + 1),
                                        STARTING_ROW + j * 2 + 22, STARTING_COLUMN + i * 18 + k * 3 + 7,
                                        market.getPossibleConversions().get(i).get(j).getResources().get(k))
                        );
                    }

                    if(market.getPossibleConversions().get(i).get(j).getFaithPoints() > 0)
                        groupBox.addElement(
                                new ResourceBoxWithAmount("conversion_" + (i + 1) + "_" + (j + 1) + "_faith",
                                        STARTING_ROW + j * 2 + 22, STARTING_COLUMN + i * 18 + k * 3 + 7 + 2,
                                        "faith",
                                        market.getPossibleConversions().get(i).get(j).getFaithPoints())
                    );
                }
            }
            else
                conversions[i].setVisible(false);
    }
}
