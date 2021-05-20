package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.Arrays;

public class DiscountHolderCliUpdater implements Listener<ClientDiscountHolder> {
    public static final int STARTING_ROW = 12;
    public static final int STARTING_COLUMN = 37;

    private final Frame frame;
    private ClientDiscountHolder discountHolder;

    private ResourceBox gold, stone, servant, shield;
    private TextBox txt_gold, txt_stone, txt_servant, txt_shield;
    private GroupBox groupBox;

    public DiscountHolderCliUpdater(ClientDiscountHolder discountHolder, Frame frame) {
        if(frame == null || discountHolder == null)
            throw new NullPointerException();

        this.frame = frame;
        this.discountHolder = discountHolder;

        discountHolder.addListener(this);

        setup(discountHolder);
        update(discountHolder);
    }

    public void setup(ClientDiscountHolder clientDiscountHolder) {
        gold = new ResourceBox("discount_holder_gold", STARTING_ROW + 1, STARTING_COLUMN + 2, "gold");
        stone = new ResourceBox("discount_holder_stone", STARTING_ROW + 1, STARTING_COLUMN + 7, "stone");
        servant = new ResourceBox("discount_holder_servant", STARTING_ROW + 1, STARTING_COLUMN + 12, "servant");
        shield = new ResourceBox("discount_holder_shield", STARTING_ROW + 1, STARTING_COLUMN + 17, "shield");

        txt_gold = new TextBox("discount_holder_gold_textbox", STARTING_ROW + 3, STARTING_COLUMN + 2, "  ",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK);

        txt_stone = new TextBox("discount_holder_stone_textbox", STARTING_ROW + 3, STARTING_COLUMN + 7, "  ",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK);

        txt_servant = new TextBox("discount_holder_servant_textbox", STARTING_ROW + 3, STARTING_COLUMN + 12, "  ",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK);

        txt_shield = new TextBox("discount_holder_shield_textbox", STARTING_ROW + 3, STARTING_COLUMN + 17, "  ",
                ForegroundColor.WHITE_BRIGHT,
                BackgroundColor.BLACK);

        groupBox = new GroupBox("discount_holder_box", STARTING_ROW, STARTING_COLUMN, STARTING_ROW + 4, STARTING_COLUMN + 20,
                "Discounts", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK,
                Arrays.asList(gold, stone, servant, shield, txt_gold, txt_stone, txt_servant, txt_shield));

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientDiscountHolder clientDiscountHolder) {
        if(!clientDiscountHolder.getDiscounts().containsKey("gold") || clientDiscountHolder.getDiscounts().get("gold") == 0)
            txt_gold.setText("  ");
        else
            txt_gold.setText(String.valueOf(- clientDiscountHolder.getDiscounts().get("gold")));

        if(!clientDiscountHolder.getDiscounts().containsKey("stone") || clientDiscountHolder.getDiscounts().get("stone") == 0)
            txt_stone.setText("  ");
        else
            txt_stone.setText(String.valueOf(- clientDiscountHolder.getDiscounts().get("stone")));

        if(!clientDiscountHolder.getDiscounts().containsKey("servant") || clientDiscountHolder.getDiscounts().get("servant") == 0)
            txt_servant.setText("  ");
        else
            txt_servant.setText(String.valueOf(- clientDiscountHolder.getDiscounts().get("servant")));

        if(!clientDiscountHolder.getDiscounts().containsKey("shield") || clientDiscountHolder.getDiscounts().get("shield") == 0)
            txt_shield.setText("  ");
        else
            txt_shield.setText(String.valueOf(- clientDiscountHolder.getDiscounts().get("shield")));
    }
}
