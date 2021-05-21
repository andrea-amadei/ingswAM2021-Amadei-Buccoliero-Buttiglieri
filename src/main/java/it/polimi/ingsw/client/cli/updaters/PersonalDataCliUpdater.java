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
    public static final int INFO_STARTING_ROW = 5;
    public static final int INFO_STARTING_COLUMN = 60;

    public static final int MESSAGES_STARTING_ROW = 13;
    public static final int MESSAGES_STARTING_COLUMN = 61;

    public static final int MAX_MESSAGES = 5;

    private final Frame frame;
    private PersonalData personalData;

    private GroupBox info;
    private FixedTextBox username_label, gameName_label;
    private TextBox username, gameName;

    private Group messages;
    private List<TextBox> messageList;
    int lastMessage;


    public PersonalDataCliUpdater(PersonalData personalData, Frame frame) {
        if(frame == null || personalData == null)
            throw new NullPointerException();

        this.frame = frame;
        this.personalData = personalData;
        lastMessage = 0;

        personalData.addListener(this);

        setup(personalData);
        update(personalData);
    }

    public void setup(PersonalData personalData) {

        username_label = new FixedTextBox("username_label", INFO_STARTING_ROW + 2, INFO_STARTING_COLUMN + 4,
                15, "Username: ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        username_label.setAlignLeft(false);

        gameName_label = new FixedTextBox("game_name_label", INFO_STARTING_ROW + 4, INFO_STARTING_COLUMN + 4,
                15, "Game name: ", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);
        gameName_label.setAlignLeft(false);

        username = new TextBox("username", INFO_STARTING_ROW + 2, INFO_STARTING_COLUMN + 20, " ",
                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        gameName = new TextBox("game_name", INFO_STARTING_ROW + 4, INFO_STARTING_COLUMN + 20, " ",
                ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK);

        info = new GroupBox("info", INFO_STARTING_ROW, INFO_STARTING_COLUMN, INFO_STARTING_ROW + 6, INFO_STARTING_COLUMN + 59,
                "Info", ForegroundColor.WHITE_BRIGHT, BackgroundColor.BLACK, Arrays.asList(username_label, username, gameName_label, gameName));

        frame.addElement(info);

        messages = new Group("messages");
        messageList = new ArrayList<>(MAX_MESSAGES);
        for(int i = 0; i < MAX_MESSAGES; i++) {
            messageList.add(new TextBox("message_" + (i + 1), MESSAGES_STARTING_ROW + i, MESSAGES_STARTING_COLUMN, " ",
                    ForegroundColor.WHITE, BackgroundColor.BLACK));

            messages.addElement(messageList.get(i));
        }

        frame.addElement(messages);
    }

    @Override
    public void update(PersonalData personalData) {
        int i, j;

        username.setText(personalData.getUsername());
        gameName.setText(personalData.getGameName());

        for(i = lastMessage; i < personalData.getServerMessages().size(); i++) {
            for(j = MAX_MESSAGES - 1; j > 0; j--)
                messageList.get(j).setText(messageList.get(j - 1).getText());

            messageList.get(0).setText(personalData.getServerMessages().get(i));
        }

        lastMessage = i;
    }
}
