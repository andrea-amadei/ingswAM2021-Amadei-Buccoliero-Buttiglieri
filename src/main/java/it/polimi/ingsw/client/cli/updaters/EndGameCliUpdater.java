package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.cli.framework.elements.GroupBox;
import it.polimi.ingsw.client.cli.framework.elements.LongTextBox;
import it.polimi.ingsw.client.cli.framework.elements.TextBox;
import it.polimi.ingsw.client.clientmodel.ClientEndGameResults;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

public class EndGameCliUpdater implements Listener<ClientEndGameResults> {
    public static final int POPUP_STARTING_ROW = 15;
    public static final int POPUP_STARTING_COLUMN = 60;

    private final Frame frame;
    private GroupBox popup;
    private LongTextBox popupMessage;

    public EndGameCliUpdater(ClientEndGameResults clientEndGameResults, Frame frame){
        this.frame = frame;
        clientEndGameResults.addListener(this);

        setup();
    }

    private void setup(){
        popup = new GroupBox("popupEnd", POPUP_STARTING_ROW, POPUP_STARTING_COLUMN, POPUP_STARTING_ROW + 13, POPUP_STARTING_COLUMN + 59,
                "Game Over", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT);

        popup.setDoubleBorder(true);
        popup.setZIndex(100);
        popup.setVisible(false);

        for(int i = 0; i < 12; i++) {
            popup.addElement(
                    new TextBox("endPopupCover_" + (i + 1), POPUP_STARTING_ROW + i + 1, POPUP_STARTING_COLUMN + 1,
                            " ".repeat(58), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT)
            );
        }

        TextBox exit = new TextBox("exit", POPUP_STARTING_ROW + 11, POPUP_STARTING_COLUMN + 26,
                "[ EXIT ]", ForegroundColor.BLACK, BackgroundColor.WHITE_BRIGHT);
        exit.setZIndex(101);

        popup.addElement(exit);

        popupMessage = new LongTextBox("popupMessage", POPUP_STARTING_ROW + 3, POPUP_STARTING_COLUMN + 2, " ",
                56, 6, ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT);

        popupMessage.setZIndex(2);
        popup.addElement(popupMessage);
        frame.addElement(popup);
    }

    @Override
    public void update(ClientEndGameResults clientEndGameResults) {
        if (clientEndGameResults.isGameCrashed() || clientEndGameResults.isGameEnded()) {
            StringBuilder sb = new StringBuilder();
            if (!clientEndGameResults.isGameCrashed()) {
                sb.append("The winner is: ").append(
                        (clientEndGameResults.isHasLorenzoWon())
                                ? "Lorenzo"
                                : (clientEndGameResults.getUsernames().get(0) + " (" + clientEndGameResults.getPoints().get(0) + " points)"));

                sb.append("\n");
                if (clientEndGameResults.isHasLorenzoWon()) {
                    sb.append("2) ").append(clientEndGameResults.getUsernames().get(0)).append(" (").append(clientEndGameResults.getPoints().get(0)).append(" points)");
                } else {
                    String sep = "";
                    for (int i = 1; i < clientEndGameResults.getUsernames().size(); i++) {
                        sb.append(sep);
                        sb.append(i + 1).append(") ").append(clientEndGameResults.getUsernames().get(i)).append(" (").append(clientEndGameResults.getPoints().get(i)).append(" points)");
                        sep = "\n";
                    }
                }
            } else {
                sb.append("Our servers are down, please stand by");
            }

            popup.setVisible(true);
            popupMessage.setText(sb.toString());

            System.out.println(sb.toString());
        }
    }
}
