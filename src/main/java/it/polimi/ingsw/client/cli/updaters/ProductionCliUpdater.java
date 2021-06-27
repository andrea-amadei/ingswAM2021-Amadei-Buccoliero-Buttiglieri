package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.CliElement;
import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.ClientDiscountHolder;
import it.polimi.ingsw.client.model.ClientProduction;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.Triplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionCliUpdater implements Listener<ClientProduction> {
    public static final int STARTING_ROW = 26;
    public static final int STARTING_COLUMN = 2;

    private final Frame frame;
    private ClientProduction clientProduction;

    private GroupBox groupBox;
    private Group crafting_box;
    private Group base_box;
    private Group leader_box;

    public ProductionCliUpdater(ClientProduction clientProduction, Frame frame) {
        if(frame == null || clientProduction == null)
            throw new NullPointerException();

        this.frame = frame;
        this.clientProduction = clientProduction;

        clientProduction.addListener(this);

        setup(clientProduction);
        update(clientProduction);
    }

    public void setup(ClientProduction clientProduction) {
        crafting_box = new Group("crafting_temp");
        base_box = new Group("base_temp");
        leader_box = new Group("leader_temp");

        int i;

        for(i = 0; i < clientProduction.getUpgradableCraftingNumber(); i++) {
            crafting_box.addElement(new GroupBox("crafting_box_" + (i + 1), STARTING_ROW + 1, STARTING_COLUMN + i * 18 + 1,
                    STARTING_ROW + 8, STARTING_COLUMN + i * 18 + 18,
                    "Crafting " + (i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            crafting_box.addElement(new FixedTextBox("crafting_arrow_" + (i + 1), STARTING_ROW + 3, STARTING_COLUMN + i * 18 + 9,
                    2, "->", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            crafting_box.addElement(new FixedTextBox("crafting_empty_" + (i + 1), STARTING_ROW + 4, STARTING_COLUMN + i * 18 + 7,
                    5, "EMPTY", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            crafting_box.addElement(new FixedTextBox("crafting_plus_"  + (i + 1), STARTING_ROW + 7, STARTING_COLUMN + i * 18 + 10,
                    1, "+", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            crafting_box.addElement(new FixedTextBox("crafting_level_" + (i + 1), STARTING_ROW + 2, STARTING_COLUMN + i * 18 + 2,
                    8, "Level: 1", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            crafting_box.getElement("crafting_arrow_" + (i + 1)).setVisible(false);
            crafting_box.getElement("crafting_empty_" + (i + 1)).setZIndex(2);
            crafting_box.getElement("crafting_plus_" + (i + 1)).setVisible(false);
            crafting_box.getElement("crafting_level_" + (i + 1)).setVisible(false);
        }

        for(i = 0; i < clientProduction.getBaseCraftings().size(); i++) {
            base_box.addElement(new GroupBox("base_box_" + (i + 1), STARTING_ROW + 9, STARTING_COLUMN + i * 18 + 1,
                    STARTING_ROW + 16, STARTING_COLUMN + i * 18 + 18,
                    "Base " + (i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            base_box.addElement(new FixedTextBox("base_arrow_" + (i + 1), STARTING_ROW + 11, STARTING_COLUMN + i * 18 + 9,
                    2, "->", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            base_box.addElement(new FixedTextBox("base_plus_" + (i + 1), STARTING_ROW + 15, STARTING_COLUMN + i * 18 + 10,
                    1, "+", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            base_box.getElement("base_arrow_" + (i + 1)).setVisible(true);
            base_box.getElement("base_plus_" + (i + 1)).setVisible(false);
        }

        groupBox = new GroupBox("production", STARTING_ROW, STARTING_COLUMN,
                STARTING_ROW + 17,
                STARTING_COLUMN + 18 * clientProduction.getUpgradableCraftingNumber() + 1,
                "Production", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        for(VisibleElement e : crafting_box.getAllElements())
            groupBox.addElement(e);

        for(VisibleElement e : base_box.getAllElements())
            groupBox.addElement(e);

        frame.addElement(groupBox);
    }

    @Override
    public void update(ClientProduction clientProduction) {
        int i, j, k;
        List<String> inputs;
        List<String> outputs;
        BackgroundColor bg;

        for(VisibleElement elem : groupBox.getAllElements())
            if( elem.getName().startsWith("crafting_elem_") ||
                elem.getName().startsWith("base_elem_") ||
                elem.getName().startsWith("leader_"))
                    groupBox.removeElement(elem.getName());

        for(i = 0; i < clientProduction.getUpgradableCraftingNumber(); i++) {
            RawCrafting crafting = clientProduction.getUpgradableCraftings().get(i);

            if(clientProduction.getSelectedType() == Production.CraftingType.UPGRADABLE && clientProduction.getSelectedCraftingIndex() == i)
                bg = BackgroundColor.GREEN;
            else if(clientProduction.getUpgradableCraftingsReady().get(i))
                bg = BackgroundColor.RED_BRIGHT;
            else
                bg = BackgroundColor.BLACK;

            if(crafting == null) {
                groupBox.getElement("crafting_arrow_" + (i + 1)).setVisible(false);
                groupBox.getElement("crafting_plus_" + (i + 1)).setVisible(false);
                groupBox.getElement("crafting_level_" + (i + 1)).setVisible(false);
                groupBox.getElement("crafting_empty_" + (i + 1)).setVisible(true);
            }
            else {
                groupBox.getElement("crafting_arrow_" + (i + 1)).setVisible(true);
                groupBox.getElement("crafting_level_" + (i + 1)).setVisible(true);
                groupBox.getElement("crafting_empty_" + (i + 1)).setVisible(false);

                groupBox.getElement("crafting_plus_" + (i + 1)).setVisible(crafting.getFaithOutput() != 0);

                ((FixedTextBox) groupBox.getElement("crafting_level_" + (i + 1))).setText("Level: " + clientProduction.getUpgradableLevels().get(i));

                inputs = List.copyOf(crafting.getInput().keySet());
                outputs = List.copyOf(crafting.getOutput().keySet());

                for (j = 0; j < inputs.size(); j++) {
                    groupBox.addElement(
                            new ResourceBoxWithAmount("crafting_elem_" + (i + 1) + "_" + (j + 1),
                                    STARTING_ROW + 3 + j, STARTING_COLUMN + 2 + i * 18,
                                    inputs.get(j), crafting.getInput().get(inputs.get(j)),
                                    ForegroundColor.WHITE_BRIGHT, bg)
                    );
                }

                for (k = 0; k < outputs.size(); k++) {
                    groupBox.addElement(
                            new ResourceBoxWithAmount("crafting_elem_" + (i + 1) + "_" + (j + k + 1),
                                    STARTING_ROW + 3 + k, STARTING_COLUMN + 12 + i * 18,
                                    outputs.get(k), crafting.getOutput().get(outputs.get(k)),
                                    ForegroundColor.WHITE_BRIGHT, bg)
                    );
                }

                if(crafting.getFaithOutput() != 0)
                    groupBox.addElement(
                            new ResourceBoxWithAmount("crafting_elem_" + (i + 1) + "_" + (j + k + 1),
                                    STARTING_ROW + 7, STARTING_COLUMN + 12 + i * 18,
                                    "faith", crafting.getFaithOutput(),
                                    ForegroundColor.WHITE_BRIGHT, bg)
                    );

                ((GroupBox) groupBox.getElement("crafting_box_" + (i + 1))).setBackgroundColor(bg);
                ((FixedTextBox) groupBox.getElement("crafting_arrow_" + (i + 1))).setBackgroundColor(bg);
                ((FixedTextBox) groupBox.getElement("crafting_plus_" + (i + 1))).setBackgroundColor(bg);
                ((FixedTextBox) groupBox.getElement("crafting_level_" + (i + 1))).setBackgroundColor(bg);
            }
        }

        for(i = 0; i < clientProduction.getBaseCraftings().size(); i++) {
            RawCrafting crafting = clientProduction.getBaseCraftings().get(i);

            if(clientProduction.getSelectedType() == Production.CraftingType.BASE && clientProduction.getSelectedCraftingIndex() == i)
                bg = BackgroundColor.GREEN;
            else if(clientProduction.getBaseCraftingsReady().get(i))
                bg = BackgroundColor.RED_BRIGHT;
            else
                bg = BackgroundColor.BLACK;

            groupBox.getElement("base_plus_" + (i + 1)).setVisible(crafting.getFaithOutput() != 0);

            inputs = List.copyOf(crafting.getInput().keySet());
            outputs = List.copyOf(crafting.getOutput().keySet());

            for (j = 0; j < inputs.size(); j++) {
                groupBox.addElement(
                        new ResourceBoxWithAmount("base_elem_" + (i + 1) + "_" + (j + 1),
                                STARTING_ROW + 11 + j, STARTING_COLUMN + 2 + i * 18,
                                inputs.get(j), crafting.getInput().get(inputs.get(j)),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );
            }

            for (k = 0; k < outputs.size(); k++) {
                groupBox.addElement(
                        new ResourceBoxWithAmount("base_elem_" + (i + 1) + "_" + (j + k + 1),
                                STARTING_ROW + 11 + k, STARTING_COLUMN + 12 + i * 18,
                                outputs.get(k), crafting.getOutput().get(outputs.get(k)),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );
            }

            if(crafting.getFaithOutput() != 0)
                groupBox.addElement(
                        new ResourceBoxWithAmount("base_elem_" + (i + 1) + "_" + (j + k + 1),
                                STARTING_ROW + 15, STARTING_COLUMN + 12 + i * 18,
                                "faith", crafting.getFaithOutput(),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );

            ((GroupBox) groupBox.getElement("base_box_" + (i + 1))).setBackgroundColor(bg);
            ((FixedTextBox) groupBox.getElement("base_arrow_" + (i + 1))).setBackgroundColor(bg);
            ((FixedTextBox) groupBox.getElement("base_plus_" + (i + 1))).setBackgroundColor(bg);
        }

        for(i = 0; i < clientProduction.getLeaderCraftings().size(); i++) {
            RawCrafting crafting = clientProduction.getLeaderCraftings().get(i);

            if(clientProduction.getSelectedType() == Production.CraftingType.LEADER && clientProduction.getSelectedCraftingIndex() == i)
                bg = BackgroundColor.GREEN;
            else if(clientProduction.getLeaderCraftingsReady().get(i))
                bg = BackgroundColor.RED_BRIGHT;
            else
                bg = BackgroundColor.BLACK;

            groupBox.addElement(new GroupBox("leader_box_" + (i + 1), STARTING_ROW + 9, STARTING_COLUMN + (i + 1) * 18 + 1,
                    STARTING_ROW + 16, STARTING_COLUMN + (i + 1) * 18 + 18,
                    "Leader " + (i + 1), ForegroundColor.WHITE_BRIGHT, bg));

            groupBox.addElement(new FixedTextBox("leader_arrow_" + (i + 1), STARTING_ROW + 11, STARTING_COLUMN + (i + 1) * 18 + 9,
                    2, "->", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            groupBox.addElement(new FixedTextBox("leader_plus_" + (i + 1), STARTING_ROW + 15, STARTING_COLUMN + (i + 1) * 18 + 10,
                    1, "+", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            groupBox.getElement("leader_plus_" + (i + 1)).setVisible(crafting.getFaithOutput() != 0);

            inputs = List.copyOf(crafting.getInput().keySet());
            outputs = List.copyOf(crafting.getOutput().keySet());

            for (j = 0; j < inputs.size(); j++) {
                groupBox.addElement(
                        new ResourceBoxWithAmount("leader_elem_" + (i + 1) + "_" + (j + 1),
                                STARTING_ROW + 11 + j, STARTING_COLUMN + 2 + (i + 1) * 18,
                                inputs.get(j), crafting.getInput().get(inputs.get(j)),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );
            }

            for (k = 0; k < outputs.size(); k++) {
                groupBox.addElement(
                        new ResourceBoxWithAmount("leader_elem_" + (i + 1) + "_" + (j + k + 1),
                                STARTING_ROW + 11 + k, STARTING_COLUMN + 12 + (i + 1) * 18,
                                outputs.get(k), crafting.getOutput().get(outputs.get(k)),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );
            }

            if(crafting.getFaithOutput() != 0)
                groupBox.addElement(
                        new ResourceBoxWithAmount("leader_elem_" + (i + 1) + "_" + (j + k + 1),
                                STARTING_ROW + 15, STARTING_COLUMN + 12 + (i + 1) * 18,
                                "faith", crafting.getFaithOutput(),
                                ForegroundColor.WHITE_BRIGHT, bg)
                );

            ((FixedTextBox) groupBox.getElement("leader_arrow_" + (i + 1))).setBackgroundColor(bg);
            ((FixedTextBox) groupBox.getElement("leader_plus_" + (i + 1))).setBackgroundColor(bg);
        }
    }
}
