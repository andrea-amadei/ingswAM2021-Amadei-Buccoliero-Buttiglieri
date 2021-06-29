package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.clientmodel.ClientLeaderCards;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.parser.raw.RawRequirement;
import it.polimi.ingsw.common.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderCardsCliUpdater implements Listener<ClientLeaderCards> {
    public static final int STARTING_ROW = 26;
    public static final int STARTING_COLUMN = 61;

    private final Frame frame;
    private ClientLeaderCards leaderCards;
    private Group group;

    public LeaderCardsCliUpdater(ClientLeaderCards leaderCards, Frame frame) {
        if(frame == null || leaderCards == null)
            throw new NullPointerException();

        this.frame = frame;
        this.leaderCards = leaderCards;

        leaderCards.addListener(this);

        setup(leaderCards);
        update(leaderCards);
    }

    private int getRequirementColumn(int index) {
        if(index == 0 || index == 1)
            return 0;
        else if(index == 2 || index == 3)
            return 2;
        else
            return 1;
    }

    public void setup(ClientLeaderCards leaderCards) {
        group = new Group("cards");
        frame.addElement(group);
    }

    @Override
    public void update(ClientLeaderCards leaderCards) {
        int i, j, k;


        for(VisibleElement elem : group.getAllElements())
            group.removeElement(elem.getName());

        int tot = leaderCards.getCoveredCards() + leaderCards.getLeaderCards().size();
        GroupBox card;

        for(i = 0; i < tot; i++) {
            card = new GroupBox("card_" + (i + 1), STARTING_ROW, STARTING_COLUMN + i * 30 + 1,
                    STARTING_ROW + 21, STARTING_COLUMN + i * 30 + 28,
                    "Leader card " + (i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            card.setDoubleBorder(true);

            if(i < leaderCards.getCoveredCards()) {
                card.addElement(
                        new TextBox("covered", card.getStartingRow() + 9, card.getStartingColumn() + 11, "COVERED",
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );
            }
            else {
                card.addElement(
                        new TextBox("id", card.getStartingRow() + 1, card.getStartingColumn() + 2,
                                "Id: " + leaderCards.getLeaderCards().get(i - leaderCards.getCoveredCards()).getId(),
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

                card.addElement(
                        new TextBox("name", card.getStartingRow() + 2, card.getStartingColumn() + 2,
                                "Name: " + leaderCards.getLeaderCards().get(i - leaderCards.getCoveredCards()).getName(),
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

                card.addElement(
                        new TextBox("points", card.getStartingRow() + 3, card.getStartingColumn() + 2,
                                "Points: " + leaderCards.getLeaderCards().get(i - leaderCards.getCoveredCards()).getPoints(),
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

                card.addElement(
                        new TextBox("divider_requirements", card.getStartingRow() + 4, card.getStartingColumn(),
                                "╠══════ Requirements ══════╣",
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

                card.addElement(
                        new TextBox("divider_abilities", card.getStartingRow() + 11, card.getStartingColumn(),
                                "╠═══════ Abilities  ═══════╣",
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

                List<RawRequirement> requirements = leaderCards.getLeaderCards().get(i - leaderCards.getCoveredCards())
                        .getRequirements()
                        .stream()
                        .sorted(Comparator.comparing(RawRequirement::getType))
                        .collect(Collectors.toList());

                for(j = 0; j < requirements.size(); j++) {
                    int col = (requirements.size() > 4 ? (j / 2) : getRequirementColumn(j));

                    switch(requirements.get(j).getType()) {
                        case "flag":
                        case "level_flag":
                            int lvl = (requirements.get(j).getType().equals("flag") ? 0 : requirements.get(j).getLevel());

                            card.addElement(
                                    new FlagBoxWithAmount("requirement_" + (j + 1),
                                            card.getStartingRow() + (j % 2) * 3 + 5,
                                            card.getStartingColumn() + col * 8 + 2,
                                            lvl,
                                            requirements.get(j).getFlag().name().toLowerCase(),
                                            requirements.get(j).getAmount()
                                    )
                            );
                            break;

                        case "resource":
                            card.addElement(
                                    new ResourceBoxWithAmount("requirement_" + (j + 1),
                                            card.getStartingRow() + (j % 2) * 3 + 6,
                                            card.getStartingColumn() + col * 8 + 3,
                                            requirements.get(j).getResource(),
                                            requirements.get(j).getAmount()
                                    )
                            );
                            break;
                    }
                }

                List<RawSpecialAbility> abilities = leaderCards.getLeaderCards().get(i - leaderCards.getCoveredCards())
                        .getAbilities()
                        .stream()
                        .sorted((x, y) -> x.getType().equals("crafting") ? -1 : x.getType().compareTo(y.getType()))
                        .collect(Collectors.toList());

                boolean stop = false;
                for(j = 0; j < abilities.size() && !stop; j++) {
                    switch(abilities.get(j).getType()) {
                        case "conversion":
                            card.addElement(
                                    new MarbleBox("ability_" + (j + 1),
                                            card.getStartingRow() + j * 3 + 12,
                                            card.getStartingColumn() + 2,
                                            abilities.get(j).getFrom())
                            );

                            card.addElement(
                                    new TextBox("ability_" + (j + 1) + "_arrow",
                                            card.getStartingRow() + j * 3 + 13,
                                            card.getStartingColumn() + 10,
                                            "->", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                            );

                            for(k = 0; k < abilities.get(j).getTo().size(); k++)
                                card.addElement(
                                        new ResourceBox("ability_" + (j + 1) + "_resource_" + (k + 1),
                                                card.getStartingRow() + j * 3 + 13,
                                                card.getStartingColumn() + k * 2 + 13,
                                                abilities.get(j).getTo().get(k))
                                );

                            if(abilities.get(j).getFaithOutput() > 0)
                                card.addElement(
                                        new ResourceBoxWithAmount("ability_" + (j + 1) + "_faith",
                                                card.getStartingRow() + j * 3 + 13,
                                                card.getStartingColumn() + k * 2 + 15,
                                                "faith",
                                                abilities.get(j).getFaithOutput())
                                );
                            break;

                        case "discount":
                            card.addElement(
                                    new TextBox("ability_" + (j + 1) + "_discount",
                                            card.getStartingRow() + j * 3 + 13,
                                            card.getStartingColumn() + 2,
                                            "Discount:",
                                            ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                            );

                            card.addElement(
                                    new ResourceBox("ability_" + (j + 1) + "_resource",
                                            card.getStartingRow() + j * 3 + 13,
                                            card.getStartingColumn() + 13,
                                            abilities.get(j).getResource())
                            );

                            card.addElement(
                                    new TextBox("ability_" + (j + 1) + "_amount",
                                            card.getStartingRow() + j * 3 + 13,
                                            card.getStartingColumn() + 17,
                                            String.valueOf(- abilities.get(j).getAmount()),
                                            ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                            );
                            break;

                        case "storage":
                            card.addElement(
                                    new TextBox("ability_" + (j + 1) + "_storage",
                                            card.getStartingRow() + j * 3 + 13,
                                            card.getStartingColumn() + 2,
                                            "Storage:",
                                            ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                            );

                            for(k = 0; k < abilities.get(j).getAmount(); k++)  {
                                card.addElement(
                                        new ResourceBox("ability_" + (j + 1) + "_resource_" + (k + 1),
                                                card.getStartingRow() + j * 3 + 13,
                                                card.getStartingColumn() + 13 + k * 4,
                                                abilities.get(j).getAcceptedTypes())
                                );

                                if(k < abilities.get(j).getAmount() - 1)
                                    card.addElement(
                                            new TextBox(
                                                    "ability_" + (j + 1) + "_divider_" + (k + 1),
                                                    card.getStartingRow() + j * 3 + 13,
                                                    card.getStartingColumn() + 15 + k * 4,
                                                    "──", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK
                                            )
                                    );
                            }
                            break;

                        case "crafting":
                            Map<String, Integer> resources;
                            GroupBox crafting = new GroupBox("crafting", card.getStartingRow() + 13, card.getStartingColumn() + 2,
                                    card.getStartingRow() + 20, card.getStartingColumn() + 20, "Crafting",
                                    ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

                            crafting.addElement(
                                    new TextBox("arrow", card.getStartingRow() + 15, card.getStartingColumn() + 10,
                                            "->", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                            );

                            resources = abilities.get(j).getCrafting().getInput();
                            k = 0;
                            for (String res : resources.keySet()) {
                                crafting.addElement(
                                        new ResourceBoxWithAmount("input_" + res,
                                                card.getStartingRow() + 15 + k, card.getStartingColumn() + 3,
                                                res, resources.get(res))
                                );

                                k++;
                            }

                            resources = abilities.get(j).getCrafting().getOutput();
                            k = 0;
                            for (String res : resources.keySet()) {
                                crafting.addElement(
                                        new ResourceBoxWithAmount("output_" + res,
                                                card.getStartingRow() + 15 + k, card.getStartingColumn() + 14,
                                                res, resources.get(res))
                                );

                                k++;
                            }

                            if(abilities.get(j).getCrafting().getFaithOutput() > 0) {
                                crafting.addElement(
                                        new TextBox("plus", card.getStartingRow() + 19, card.getStartingColumn() + 11,
                                                "+", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                                );

                                crafting.addElement(
                                        new ResourceBoxWithAmount("output_faith",
                                                card.getStartingRow() + 19, card.getStartingColumn() + 14,
                                                "faith", abilities.get(j).getCrafting().getFaithOutput())
                                );
                            }

                            card.addElement(crafting);
                            stop = true;
                            break;
                    }
                }
            }

            group.addElement(card);
        }
    }
}
