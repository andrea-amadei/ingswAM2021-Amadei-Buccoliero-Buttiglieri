package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

public class PersonalDataCliUpdater implements Listener<PersonalData> {
    public static final int POPUP_STARTING_ROW = 15;
    public static final int POPUP_STARTING_COLUMN = 60;

    int messagesStartingRow;
    int messagesStartingColumn;

    public static final int MAX_LINES = 5;

    private final Frame frame;
    private PersonalData personalData;

    private LongTextBox messages;

    private GroupBox popup;
    private LongTextBox error;
    int lastError;


    public PersonalDataCliUpdater(PersonalData personalData, Frame frame, int messagesStartingRow, int messagesStartingColumn) {
        if(frame == null || personalData == null)
            throw new NullPointerException();

        this.frame = frame;
        this.personalData = personalData;

        this.messagesStartingRow = messagesStartingRow;
        this.messagesStartingColumn = messagesStartingColumn;

        lastError = 0;

        personalData.addListener(this);

        setup(personalData);
        update(personalData);
    }

    public void setup(PersonalData personalData) {
        int i;

        StringBuilder str = new StringBuilder();
        for(i = personalData.getServerMessages().size() - 1; i >= 0; i--) {
            str.append("- ").append(personalData.getServerMessages().get(i)).append('\n');
        }

        if(str.toString().isEmpty())
            str.append("[No messages so far]");

        messages = new LongTextBox("messages", messagesStartingRow, messagesStartingColumn, str.toString(), 53, MAX_LINES,
                ForegroundColor.WHITE, BackgroundColor.BLACK);
        messages.setOverflowEnabled(true);

        popup = new GroupBox("popup", POPUP_STARTING_ROW, POPUP_STARTING_COLUMN, POPUP_STARTING_ROW + 6, POPUP_STARTING_COLUMN + 59,
                "Error", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT);
        popup.setDoubleBorder(true);
        popup.setZIndex(100);
        popup.setVisible(false);

        for(i = 0; i < 5; i++) {
            popup.addElement(
                    new TextBox("cover_" + (i + 1), POPUP_STARTING_ROW + i + 1, POPUP_STARTING_COLUMN + 1,
                            " ".repeat(58), ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT)
            );
        }

        TextBox okay = new TextBox("okay", POPUP_STARTING_ROW + 4, POPUP_STARTING_COLUMN + 26,
                "[ OKAY ]", ForegroundColor.BLACK, BackgroundColor.WHITE_BRIGHT);
        okay.setZIndex(101);

        popup.addElement(okay);

        error = new LongTextBox("error", POPUP_STARTING_ROW + 1, POPUP_STARTING_COLUMN + 2, " ",
                56, 3, ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT);
        error.setZIndex(2);

        popup.addElement(error);

        frame.addElement(popup);
        frame.addElement(messages);
    }

    @Override
    public void update(PersonalData personalData) {
        int i, j;



        if(popup.isVisible() && !personalData.isErrorConfirmed())
            return;

        if(lastError < personalData.getServerErrors().size()) {
            error.setText(personalData.getServerErrors().get(lastError));
            lastError++;
            popup.setVisible(true);
            personalData.setErrorConfirmed(false);
        }

        if(personalData.isErrorConfirmed())
            popup.setVisible(false);
    }
}
