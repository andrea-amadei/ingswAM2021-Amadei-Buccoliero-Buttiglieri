package it.polimi.ingsw.client.cli.updaters;

import it.polimi.ingsw.client.cli.framework.elements.*;
import it.polimi.ingsw.client.clientmodel.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.common.utils.BackgroundColor;
import it.polimi.ingsw.common.utils.ForegroundColor;

import java.util.Arrays;

public class LobbyPersonalDataCliUpdater implements Listener<PersonalData> {
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

    private LongTextBox messages;


    public LobbyPersonalDataCliUpdater(PersonalData personalData, Frame frame) {
        if(frame == null || personalData == null)
            throw new NullPointerException();

        this.frame = frame;
        this.personalData = personalData;

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

        messages = new LongTextBox("messages", MESSAGES_STARTING_ROW, MESSAGES_STARTING_COLUMN, "[No messages so far]", 53, 6,
                ForegroundColor.WHITE, BackgroundColor.BLACK);
        messages.setOverflowEnabled(true);

        frame.addElement(messages);
    }

    @Override
    public void update(PersonalData personalData) {
        int i, j;

        username.setText(personalData.getUsername());
        gameName.setText(personalData.getGameName());

        StringBuilder str = new StringBuilder();
        for(i = personalData.getServerMessages().size() - 1; i >= 0; i--) {
            str.append("- ").append(personalData.getServerMessages().get(i)).append('\n');
        }

        if(str.toString().isEmpty())
            str.append("[No messages so far]");

        messages.setText(str.toString());
    }
}
