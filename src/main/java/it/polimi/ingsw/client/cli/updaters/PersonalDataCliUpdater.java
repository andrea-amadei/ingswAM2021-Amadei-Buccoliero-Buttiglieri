package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalDataCliUpdater implements Listener<PersonalData> {
    public static final int POPUP_STARTING_ROW = 15;
    public static final int POPUP_STARTING_COLUMN = 60;

    int messagesStartingRow;
    int messagesStartingColumn;

    public static final int MAX_MESSAGES = 5;

    private final Frame frame;
    private PersonalData personalData;

    private Group messages;
    private List<TextBox> messageList;
    int lastMessage;

    private GroupBox popup;
    private TextBox error;
    int lastError;


    public PersonalDataCliUpdater(PersonalData personalData, Frame frame, int messagesStartingRow, int messagesStartingColumn) {
        if(frame == null || personalData == null)
            throw new NullPointerException();

        this.frame = frame;
        this.personalData = personalData;

        this.messagesStartingRow = messagesStartingRow;
        this.messagesStartingColumn = messagesStartingColumn;

        lastMessage = 0;
        lastError = 0;

        personalData.addListener(this);

        setup(personalData);
        update(personalData);
    }

    public void setup(PersonalData personalData) {
        int i;

        messages = new Group("messages");
        messageList = new ArrayList<>(MAX_MESSAGES);
        for(i = 0; i < MAX_MESSAGES; i++) {
            messageList.add(new TextBox("message_" + (i + 1), messagesStartingRow + i, messagesStartingColumn, " ",
                    ForegroundColor.WHITE, BackgroundColor.BLACK));

            messages.addElement(messageList.get(i));
        }

        frame.addElement(messages);

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

        popup.addElement(
                new TextBox("okay", POPUP_STARTING_ROW + 4, POPUP_STARTING_COLUMN + 26,
                        "[ OKAY ]", ForegroundColor.BLACK, BackgroundColor.WHITE_BRIGHT)
        );

        error = new TextBox("error", POPUP_STARTING_ROW + 2, POPUP_STARTING_COLUMN + 2, " ",
                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK_BRIGHT);
        error.setZIndex(2);
        popup.addElement(error);

        frame.addElement(popup);
    }

    @Override
    public void update(PersonalData personalData) {
        int i, j;

        for(i = lastMessage; i < personalData.getServerMessages().size(); i++) {
            for(j = MAX_MESSAGES - 1; j > 0; j--)
                messageList.get(j).setText(messageList.get(j - 1).getText());

            messageList.get(0).setText(personalData.getServerMessages().get(i));
        }

        lastMessage = i;

        if(lastError < personalData.getServerErrors().size()) {
            error.setText(personalData.getServerErrors().get(lastError));
            lastError++;
            popup.setVisible(true);
            personalData.setMessageConfirmed(false);
        }

        if(personalData.isMessageConfirmed())
            popup.setVisible(false);
    }
}
