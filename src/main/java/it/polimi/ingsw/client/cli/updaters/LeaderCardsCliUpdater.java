package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.framework.elements.GroupBox;
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
    private List<GroupBox> cards;

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
        int tot = leaderCards.getCoveredCards() + leaderCards.getLeaderCards().size();

        cards = new ArrayList<>(tot);

        for(int i = 0; i < tot; i++) {
            cards.add(new GroupBox("card_" + (i + 1), STARTING_ROW, STARTING_COLUMN + i * 20 + 1, STARTING_ROW + 20, STARTING_COLUMN + i * 20 + 20,
                    "Card " + (i + 1), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK));

            frame.addElement(cards.get(i));
        }
    }

    @Override
    public void update(ClientLeaderCards leaderCards) {

    }
}
