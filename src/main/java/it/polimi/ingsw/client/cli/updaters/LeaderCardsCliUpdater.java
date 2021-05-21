package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.VisibleElement;
import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.framework.elements.Group;
import it.polimi.ingsw.client.cli.framework.elements.GroupBox;
import it.polimi.ingsw.client.cli.framework.elements.TextBox;
import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.List;

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

    public void setup(ClientLeaderCards leaderCards) {
        group = new Group("cards");
        frame.addElement(group);
    }

    @Override
    public void update(ClientLeaderCards leaderCards) {
        for(VisibleElement elem : group.getAllElements())
            group.removeElement(elem.getName());

        int tot = leaderCards.getCoveredCards() + leaderCards.getLeaderCards().size();
        GroupBox card;

        for(int i = 0; i < tot; i++) {
            card = new GroupBox("card_" + (i + 1), STARTING_ROW, STARTING_COLUMN + i * 20 + 1,
                    STARTING_ROW + 20, STARTING_COLUMN + i * 20 + 20,
                    "Leader " + (i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

            if(i < leaderCards.getCoveredCards())
                card.addElement(
                        new TextBox("covered", STARTING_ROW + 9, STARTING_COLUMN + 7 + i * 20, "COVERED",
                                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK)
                );

            group.addElement(card);
        }
    }
}
